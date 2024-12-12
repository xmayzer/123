package com.example.a123.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Entity(tableName = "notes")
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val noteTitle: String,
    val noteDesc: String,
    val moodImageId: Int = 0,
    val date: Long
):Parcelable {
    // Метод для получения даты без времени (только день-месяц-год)
    fun getDateWithoutTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date

        // Сбрасываем время (часы, минуты, секунды, миллисекунды)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis
    }
}