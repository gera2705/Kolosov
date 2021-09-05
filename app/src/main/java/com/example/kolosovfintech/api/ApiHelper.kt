package com.example.kolosovfintech.api

class ApiHelper(private val apiServiceService: ApiService) {
    suspend fun getGif(category: String, pageNumber: Int) =
        apiServiceService.getGifsCategoryList(category = category, pageNumber = pageNumber)
}
