package com.example.a123.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.a123.MainActivity
import com.example.a123.R
import com.example.a123.databinding.FragmentAddBinding
import com.example.a123.model.Note
import com.example.a123.viewmodel.NoteViewModel
import java.util.Calendar

class AddFragment : Fragment(R.layout.fragment_add){

   private var addNoteBinding: FragmentAddBinding? = null
    private val binding get() = addNoteBinding!!

    private lateinit var buttonUndo: Button
    private lateinit var buttonSave: Button

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var addNoteView: View

    private var selectedMoodImageId: Int = -1
    private lateinit var moodImages: Array<ImageView>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addNoteBinding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        notesViewModel = (activity as MainActivity).noteViewModel
        addNoteView = view

        buttonSave = view.findViewById(R.id.buttonSave)
        buttonSave.setOnClickListener {
            saveNote(it)
        }

        buttonUndo = view.findViewById(R.id.buttonUndo)

        buttonUndo.setOnClickListener {
            // Переход в NotesFragment
            binding.addNoteTitle.text.clear()
            binding.addNoteDesc.text.clear()
            parentFragmentManager.popBackStack() // Возвращаемся к предыдущему фрагменту
        }
        // Инициализация массива изображений настроений
        moodImages = arrayOf(
            binding.imageStar1,
            binding.imageStar2,
            binding.imageStar3,
            binding.imageStar4,
            binding.imageStar5
        )
        moodImages.forEachIndexed { index, imageView ->
            val moodImageRes = resources.getIdentifier("star${index + 1}", "drawable", context?.packageName)
            imageView.setImageResource(moodImageRes)
            imageView.setOnClickListener {
                selectedMoodImageId = moodImageRes
                highlightSelectedImage(index)
            }
        }
    }
    private fun saveNote(view: View){
        val noteTitle = binding.addNoteTitle.text.toString().trim()
        val noteDesc = binding.addNoteDesc.text.toString().trim()

        if (noteTitle.isNotEmpty()){
            val currentDateWithoutTime = getCurrentDateWithoutTime()
            val note = Note(0, noteTitle, noteDesc, selectedMoodImageId, date = currentDateWithoutTime)
            notesViewModel.addNote(note)

            Toast.makeText(addNoteView.context, "Сохранено", Toast.LENGTH_SHORT).show()

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, NotesFragment())
            transaction.addToBackStack(null) // Добавляем в стек для возможности возврата
            transaction.commit()
        } else {
            Toast.makeText(addNoteView.context, "Введите название заголовка", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentDateWithoutTime(): Long {
        // Получаем текущее время
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()

        // Сбрасываем время (чтобы оставить только день и год)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // Возвращаем время в миллисекундах, которое будет соответствовать началу дня (00:00:00)
        return calendar.timeInMillis
    }

    private fun highlightSelectedImage(moodImageId: Int) {
        // Убедитесь, что moodImages инициализирован до вызова этой функции
        for (imageView in moodImages) {
            imageView.setAlpha(0.5f) // Устанавливаем прозрачность для невыбранных изображений
        }

        // Выбираем соответствующее изображение в зависимости от moodImageId
        val selectedImage = moodImages.getOrNull(moodImageId)
        selectedImage?.setAlpha(1.0f) // Устанавливаем полную непрозрачность для выбранного изображения
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    
}