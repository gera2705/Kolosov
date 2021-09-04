package com.example.kolosovfintech.api

class ApiHelper(private val apiServiceService: ApiService) {
    suspend fun getUsers() = apiServiceService.getUsers()
}
