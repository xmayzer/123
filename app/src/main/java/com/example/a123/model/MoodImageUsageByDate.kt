package com.example.a123.model

import androidx.room.ColumnInfo

data class MoodImageUsageByDate(
    @ColumnInfo(name = "date") val date: Long, // Дата
    @ColumnInfo(name = "moodImageId") val moodImageId: Int, // Идентификатор картинки настроения
    @ColumnInfo(name = "count") val count: Int // Количество картинок для конкретной даты и типа
)