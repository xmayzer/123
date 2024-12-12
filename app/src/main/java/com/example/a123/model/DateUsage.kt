package com.example.a123.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "notes")
data class DateUsage(
    @ColumnInfo(name = "date") val date: Long, // Ссылается на столбец "date"
    @ColumnInfo(name = "count") val count: Int // Ссылается на результат COUNT(*)

)