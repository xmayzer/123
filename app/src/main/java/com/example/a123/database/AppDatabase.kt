package com.example.a123.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.a123.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}