package com.example.a123.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.a123.User

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE login = :login AND password = :password LIMIT 1")
    suspend fun getUserByLoginAndPassword(login: String, password: String): User?

    @Query("SELECT * FROM users WHERE login = :login LIMIT 1")
    suspend fun getUserByLogin(login: String): User?
}