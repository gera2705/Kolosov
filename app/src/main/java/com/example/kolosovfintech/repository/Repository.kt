package com.example.kolosovfintech.repository

import com.example.kolosovfintech.model.Post
import com.example.kolosovfintech.api.RetrofitInstance
import retrofit2.Response

class Repository {

    suspend fun getPost(): Response<Post> {
        return RetrofitInstance.api.getPost()
    }
}