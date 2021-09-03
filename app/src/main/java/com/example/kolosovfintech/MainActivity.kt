package com.example.kolosovfintech

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.kolosovfintech.databinding.ActivityMainBinding
import com.example.kolosovfintech.model.Post
import com.example.kolosovfintech.repository.Repository
import com.example.rtest.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private val binding by viewBinding(ActivityMainBinding::bind)

    private var gifsList: ArrayList<Post>? = arrayListOf<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repo = Repository()
        val viewModelFactory = MainViewModelFactory(repo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)


        viewModel.getPost()
        //viewModel.getPost()

        getRandomGif()

        binding.nextButton.setOnClickListener {

            viewModel.getPost()
            getRandomGif()

//            viewModel.getPost()
//            viewModel.apiResponse.observe(this, Observer { responce ->
//                Log.d("Resp", responce.body().toString())
//                if (responce.isSuccessful) {
//                    Log.d("Resp", responce.body()?.description.toString())
//                    setGif(responce.body()?.gifURL.toString())
//                } else {
//                    Log.d("Resp", responce.errorBody().toString())
//                }
//
//            })
        }
    }

    private fun getRandomGif() {

        viewModel.apiResponse.observe(this, Observer { response ->

            if (response.isSuccessful) {
                Log.d("Resp", response.body()?.description.toString())
                setDescription(response.body()?.description.toString())
                setGif(response.body()?.gifURL.toString())

                Log.d("LIST" , gifsList?.size.toString())
                Log.d("LIST" , gifsList.toString())

            } else {
                Log.d("Resp", response.errorBody().toString())
            }
        })



    }

    private fun setGif(URL: String) {

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

    private fun setDescription(description: String) {
        binding.descriptionText.text = description
    }
}
