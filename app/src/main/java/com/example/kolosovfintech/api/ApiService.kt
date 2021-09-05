package com.example.kolosovfintech.api

import com.example.kolosovfintech.model.Data
import com.example.kolosovfintech.model.Post
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("random?json=true")
    suspend fun getRandomGif(): Post

    @GET("/{category}/{pageNumber}?json=true")
    suspend fun getGifsCategoryList(
        @Path("category") category: String,
        @Path("pageNumber") pageNumber: Int
    ): Data
}
