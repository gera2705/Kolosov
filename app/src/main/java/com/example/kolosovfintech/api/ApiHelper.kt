package com.example.kolosovfintech.api

import retrofit2.http.Path

class ApiHelper(private val apiServiceService: ApiService) {
    suspend fun getGif(category:String , pageNumber:Int ) = apiServiceService.getGifsCategoryList(category = category, pageNumber = pageNumber)
}
