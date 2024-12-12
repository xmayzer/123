package com.example.a123.network
import com.example.a123.model.Tip
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("get_random_tip")
    suspend fun getRandomTip(): Tip
}