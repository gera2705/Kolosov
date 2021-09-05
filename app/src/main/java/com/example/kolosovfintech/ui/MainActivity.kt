package com.example.kolosovfintech.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.example.kolosovfintech.R
import com.example.kolosovfintech.Status
import com.example.kolosovfintech.api.ApiHelper
import com.example.kolosovfintech.api.RetrofitInstance
import com.example.kolosovfintech.databinding.ActivityMainBinding
import com.example.kolosovfintech.model.Data
import com.example.kolosovfintech.model.Post
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private val binding by viewBinding(ActivityMainBinding::bind)

    private var gifsList: ArrayList<Post>? = arrayListOf()
    private var positionCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()

        binding.rebootButton.isVisible = false
        binding.rebootButton.isEnabled = false

        when (Id.itemNumber) {
            1 -> getGif("top", (0..100).random())
            2 -> getGif("latest", (0..100).random())
            3 -> getGif("hot", (0..100).random())
        }

        binding.nextButton.setOnClickListener {

            if (positionCount == 5) {
                positionCount = 1
                gifsList?.clear()
                binding.gifImg.setImageResource(0)

                when (Id.itemNumber) {
                    1 -> getGif("latest", (0..100).random())
                    2 -> getGif("top", (0..100).random())
                    3 -> getGif("hot", (0..100).random())
                }

            } else {
                setGif(gifsList?.get(positionCount)?.gifURL)
                setDescription(gifsList?.get(positionCount)?.description)
                positionCount++
            }
        }

        binding.latestButton.setOnClickListener {
            binding.gifImg.setImageResource(0)
            gifsList?.clear()
            Id.itemNumber = 1
            this.recreate()
        }

        binding.hotButton.setOnClickListener {
            binding.gifImg.setImageResource(0)
            gifsList?.clear()
            Id.itemNumber = 3
            this.recreate()
        }

        binding.topButton.setOnClickListener {
            binding.gifImg.setImageResource(0)
            gifsList?.clear()
            Id.itemNumber = 2
            this.recreate()
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

        binding.rebootButton.setOnClickListener {

            when (Id.itemNumber) {
                1 -> getGif("latest", (0..100).random())
                2 -> getGif("top", (0..100).random())
                3 -> getGif("hot", (0..100).random())
            }

            binding.rebootButton.isVisible = false
            binding.rebootButton.isEnabled = false

        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            MainViewModelFactory(ApiHelper(RetrofitInstance.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun setGif(URL: String?) {
        try {
            Glide.with(this@MainActivity)
                .asGif()
                .load(URL)
                .placeholder(R.drawable.progress_bar)
                .error(R.drawable.ic_error_image)
                .fallback(R.drawable.ic_error_image)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.gifImg)
        }catch (e : GlideException){
            binding.rebootButton.isVisible = true

        }
    }

    private fun setDescription(description: String?) {
        binding.descriptionText.text = description
    }

    private fun getGif(category: String, page: Int) {
        viewModel.getGif(category, page).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {

                        binding.nextButton.isEnabled = true
                        binding.backButton.isEnabled = true

                        val post: Data? = resource.data

                        Log.d("TAG", post.toString())

                        post?.dataModelList.let { it1 ->
                            if (it1 != null) {
                                gifsList?.addAll(it1)
                            }
                        }

                        Log.d("TAG", gifsList.toString())

                        if(gifsList?.size == 0){
                            binding.gifImg.setImageResource(R.drawable.ic_error_image)
                        }
                        setGif(post!!.dataModelList[0].gifURL)
                        setDescription(post.dataModelList[0].description)
                    }
                    Status.ERROR -> {
                        binding.rebootButton.isVisible = true
                        binding.gifImg.setImageResource(R.drawable.ic_error_image)
                        binding.descriptionText.text = "Ошибка"
                        binding.rebootButton.isEnabled = true

                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        binding.nextButton.isEnabled = false
                        binding.backButton.isEnabled = false
                        binding.loadingBar.visibility = View.GONE
                        Toast.makeText(this, "Загрузка", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}

object Id {
    var itemNumber: Int? = 1
}
