package com.example.kolosovfintech

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kolosovfintech.model.Post
import com.example.kolosovfintech.repository.Repository

import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {

    val apiResponse: MutableLiveData<Response<Post>> = MutableLiveData()

    fun getPost() {
        viewModelScope.launch {
            val response = repository.getPost()
            apiResponse.value = response
        }
    }
}