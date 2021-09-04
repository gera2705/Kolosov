package com.example.kolosovfintech.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.kolosovfintech.Resource
import com.example.kolosovfintech.repository.Repository
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val repository: Repository): ViewModel() {

    fun getUsers() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getUsers()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}