package com.example.kolosovfintech.repository

import com.example.kolosovfintech.api.ApiHelper

class Repository(private val apiHelper: ApiHelper) {
    suspend fun getGif(category: String, pageNumber: Int) =
        apiHelper.getGif(category = category, pageNumber = pageNumber)
}
