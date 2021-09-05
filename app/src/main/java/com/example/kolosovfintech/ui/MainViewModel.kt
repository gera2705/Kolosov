package com.example.kolosovfintech.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.kolosovfintech.Resource
import com.example.kolosovfintech.repository.Repository
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val repository: Repository): ViewModel() {

    fun getGif(category:String , pageNumber:Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = repository.getGif(category = category, pageNumber = pageNumber)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            Log.d("TAG3" , exception.toString())
        }
    }
}
