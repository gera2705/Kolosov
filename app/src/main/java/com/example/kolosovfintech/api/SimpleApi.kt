package com.example.kolosovfintech.api

import com.example.kolosovfintech.model.Post
import retrofit2.Response
import retrofit2.http.GET


interface SimpleApi {

    @GET("random?json=true")
    suspend fun getPost(): Response<Post>

}