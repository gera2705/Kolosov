package com.example.kolosovfintech.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.kolosovfintech.R
import com.example.kolosovfintech.Status
import com.example.kolosovfintech.api.ApiHelper
import com.example.kolosovfintech.api.RetrofitInstance
import com.example.kolosovfintech.databinding.ActivityMainBinding
import com.example.kolosovfintech.model.Post

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private val binding by viewBinding(ActivityMainBinding::bind)

    private var gifsList: ArrayList<Post>? = arrayListOf<Post>()
    private var positionCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
        setupObservers()

        binding.nextButton.setOnClickListener {
            if (positionCount == gifsList?.size) {
                setupObservers()
            } else {
                setGif(gifsList?.get(positionCount)?.gifURL)
                setDescription(gifsList?.get(positionCount)?.description)
            }
            positionCount++
        }

        binding.backButton.setOnClickListener {
            if (positionCount - 1 == 0) {
                Toast.makeText(this@MainActivity, "Гифки сзади закончились :(", Toast.LENGTH_SHORT)
                    .show()
            } else {
                setGif(gifsList?.get(positionCount - 2)?.gifURL)
                setDescription(gifsList?.get(positionCount - 2)?.description)
                positionCount--
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            MainViewModelFactory(ApiHelper(RetrofitInstance.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun setGif(URL: String?) {
        Glide.with(this@MainActivity)
            .asGif()
            .load(URL)
            .placeholder(R.drawable.progress_bar)
            .error(R.drawable.ic_error_image)
            .fallback(R.drawable.ic_error_image)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(binding.gifImg)
    }

    private fun setDescription(description: String?) {
        binding.descriptionText.text = description
    }

    private fun setupObservers() {
        viewModel.getUsers().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {

                        val post:Post? = resource.data
                        gifsList?.add(post!!)

                        setGif(post!!.gifURL)
                        setDescription(post.description)

                        Log.d("TAG" , resource.data.toString())
                        Log.d("TAG" , gifsList?.size.toString())
                        Log.d("TAG" , gifsList.toString())
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }
}
