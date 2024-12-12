package com.example.a123.model

import androidx.room.ColumnInfo

data class MoodImageUsage(
    @ColumnInfo(name = "moodImageId") val moodImageId: Int,
    @ColumnInfo(name = "COUNT(*)") val usageCount: Int
)