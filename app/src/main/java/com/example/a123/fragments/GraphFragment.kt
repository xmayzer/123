package com.example.a123.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.room.InvalidationTracker
import com.example.a123.MainActivity
import com.example.a123.R
import com.example.a123.databinding.FragmentGraphBinding
import com.example.a123.model.DateUsage
import com.example.a123.model.MoodImageUsage
import com.example.a123.model.MoodImageUsageByDate
import com.example.a123.viewmodel.NoteViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.Calendar


class GraphFragment : Fragment(R.layout.fragment_graph) {

    private lateinit var barChart: BarChart
    private lateinit var pieChart: PieChart
    private lateinit var notesViewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGraphBinding.inflate(inflater, container, false)

        // Инициализация элементов графиков через binding
        barChart = binding.barChart
        pieChart = binding.pieChart

        notesViewModel = (activity as MainActivity).noteViewModel

        notesViewModel.moodImageUsageCounts.observe(viewLifecycleOwner, Observer { usageCounts ->
            setupPieChart(usageCounts) // Обновляем график
        })

        notesViewModel.getNotesCountByDateAndMood().observe(viewLifecycleOwner, Observer { moodImageUsageByDateList ->
            setupBarChart(moodImageUsageByDateList) // Обновляем график
        })

        return binding.root
    }


    private fun setupBarChart(moodImageUsageByDateList: List<MoodImageUsageByDate>) {
        val barEntries = mutableListOf<BarEntry>()

        // Получаем текущий месяц и год
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        // Фильтруем данные только для текущего месяца и года
        val filteredData = moodImageUsageByDateList.filter { usage ->
            val usageDate = Date(usage.date)
            val usageCalendar = Calendar.getInstance().apply { time = usageDate }
            val usageMonth = usageCalendar.get(Calendar.MONTH)
            val usageYear = usageCalendar.get(Calendar.YEAR)

            usageMonth == currentMonth && usageYear == currentYear
        }

        // Группируем данные по дате
        val groupedByDate = filteredData.groupBy { it.date }

        // Индекс для оси X
        var index = 0f

        // Преобразуем данные для графика
        groupedByDate.forEach { (date, usages) ->
            val moodCounts = IntArray(5) { 0 } // 5 настроений

            // Считаем количество каждого настроения для дня
            usages.forEach { usage ->
                val moodIndex = getMoodIndex(usage.moodImageId)
                if (moodIndex != -1) {
                    moodCounts[moodIndex] += usage.count
                }
            }

            // Создаем столбик для текущего дня
            val entryValues = moodCounts.map { it.toFloat() }
            barEntries.add(BarEntry(index, entryValues.toFloatArray()))

            // Увеличиваем индекс для следующего столбика
            index += 1f
        }

        // Настроим набор данных для графика
        val dataSet = BarDataSet(barEntries, "Количество заметок по дням")

        // Устанавливаем разные цвета для каждого настроения
        val colors = getMoodColorsForBarChart()
        dataSet.setColors(colors) // Применяем цвета для столбцов

        // Отключаем отображение чисел над столбиками
        dataSet.setDrawValues(false)

        // Настроим внешний вид данных
        dataSet.valueTextColor = Color.WHITE // Цвет текста значений (не будет видно, т.к. отключены значения)
        dataSet.valueTextSize = 12f // Размер текста значений

        // Настроим данные для графика
        val data = BarData(dataSet)
        barChart.data = data

        // Настроим внешний вид графика
        barChart.apply {
            // Создаем список меток для оси X (для каждого столбика)
            val xLabels = groupedByDate.keys.map { date ->
                val formattedDate = SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(date))
                formattedDate // Форматируем дату
            }

            // Устанавливаем форматирование оси X
            xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)

            // Настройки оси X
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.setDrawAxisLine(false) // Убираем линию оси X
            xAxis.setDrawGridLines(false) // Убираем сетку оси X

            // Настройки оси Y
            axisLeft.axisMinimum = 0f
            axisLeft.setDrawAxisLine(false) // Убираем линию оси Y
            axisLeft.setDrawGridLines(false) // Убираем сетку оси Y
            axisRight.isEnabled = false

            // Убираем линии фона (сетку и линии)
            setDrawGridBackground(false) // Убираем фон с разметкой
            setDrawBorders(false) // Убираем рамки

            // Отключаем легенду
            legend.isEnabled = false

            description.isEnabled = false

            // Анимация
            animateY(1500, Easing.EaseInOutCubic)  // Плавная анимация столбиков
        }

        barChart.invalidate() // Перерисовываем график
    }



    private fun getMoodIndex(moodImageId: Int): Int {
        return when (moodImageId) {
            R.drawable.star1 -> 0 // Плохо
            R.drawable.star2 -> 1 // Не очень
            R.drawable.star3 -> 2 // Сойдет
            R.drawable.star4 -> 3 // Нормально
            R.drawable.star5 -> 4 // Отлично
            else -> -1 // Неизвестное изображение
        }
    }

    private fun getMoodColorsForBarChart(): List<Int> {
        return listOf(
            Color.parseColor("#FFCCCC"), // Плохо (светло-красный)
            Color.parseColor("#FFD699"), // Не очень (светло-оранжевый)
            Color.parseColor("#ADD8E6"), // Сойдет (светло-голубой)
            Color.parseColor("#98FB98"), // Нормально (светло-зеленый)
            Color.parseColor("#A9D0A7")  // Отлично (светло-зеленый)
        )
    }

    private fun setupPieChart(usageCounts: List<MoodImageUsage>) {
        val entries = mutableListOf<PieEntry>()
        val labels = mutableListOf<String>()

        // Преобразуем данные для графика
        usageCounts.forEach {
            val label = getMoodLabel(it.moodImageId) // Преобразуем moodImageId в метку
            entries.add(PieEntry(it.usageCount.toFloat(), label))
            labels.add(label)
        }

        val colors = getMoodColors(usageCounts)

        // набор данных для графика
        val dataSet = PieDataSet(entries, "Настроение") // Добавляем заголовок к графику
        dataSet.setColors(colors) // Передаем список цветов
        dataSet.sliceSpace = 3f // Расстояние между секторами
        dataSet.setDrawValues(true) // Показывать значения внутри секторов
        dataSet.valueTextColor = Color.WHITE // Цвет текста значений
        dataSet.valueTextSize = 12f // Размер шрифта для значений
        dataSet.setValueLineColor(Color.WHITE) // Цвет линии, соединяющей значения с секторами
        dataSet.setValueLinePart1Length(0.5f) // Длина линии значений
        dataSet.setValueLinePart2Length(0.5f) // Длина линии значений

        val data = PieData(dataSet)

        pieChart.apply {
            this.data = data
            description.isEnabled = false // Отключаем описание
            isDrawHoleEnabled = true // Рисуем дырку в середине (для donut chart)
            holeRadius = 50f // Радиус дырки
            transparentCircleRadius = 61f // Прозрачная область вокруг дырки
            rotationAngle = 0f // Убираем начальный угол (по умолчанию график немного поворачивается)
            setCenterText("Настроения") // Текст в центре графика
            setCenterTextSize(16f) // Размер текста в центре

            animateY(1000, Easing.EaseInOutCubic) // Анимация раскрашивания графика


            legend.isEnabled = false
        }

        // Обновляем график
        pieChart.invalidate()
    }

    private fun getMoodLabel(moodImageId: Int): String {
        // Преобразуем ID картинки настроения в текстовое описание
        return when (moodImageId) {
            R.drawable.star1 -> "Плохо"
            R.drawable.star2 -> "Не очень"
            R.drawable.star3 -> "Сойдет"
            R.drawable.star4 -> "Нормально"
            R.drawable.star5 -> "Отлично"
            else -> ""
        }
    }

    private fun getMoodColors(usageCounts: List<MoodImageUsage>): List<Int> {
        val colors = mutableListOf<Int>()

        usageCounts.forEach {
            val color = when (getMoodLabel(it.moodImageId)) {
                "Плохо" -> Color.parseColor("#FFCCCC") // Светло-красный для "Плохо"
                "Не очень" -> Color.parseColor("#FFD699") // Светло-оранжевый для "Не очень"
                "Сойдет" -> Color.parseColor("#ADD8E6") // Светло-голубой для "Сойдет"
                "Нормально" -> Color.parseColor("#98FB98") // Светло-зеленый для "Нормально"
                "Отлично" -> Color.parseColor("#A9D0A7") // Светло-зеленый для "Отлично"
                else -> Color.GRAY // Для прочих случаев (если нужно)
            }
            colors.add(color)
        }

        return colors
    }
}

