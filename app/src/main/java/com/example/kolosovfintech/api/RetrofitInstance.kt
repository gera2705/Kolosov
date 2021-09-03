package com.example.kolosovfintech.api


import com.example.kolosovfintech.util.Constants
import com.example.rtest.api.SimpleApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }


    val api: SimpleApi by lazy{
        retrofit.create(SimpleApi::class.java)
    }
}