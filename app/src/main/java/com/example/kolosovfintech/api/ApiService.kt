package com.example.kolosovfintech.api

import com.example.kolosovfintech.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET("random?json=true")
    suspend fun getUsers(): Post
}
