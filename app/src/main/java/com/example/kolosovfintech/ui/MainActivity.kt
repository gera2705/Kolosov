package com.example.kolosovfintech.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.kolosovfintech.R
import com.example.kolosovfintech.util.Status
import com.example.kolosovfintech.api.ApiHelper
import com.example.kolosovfintech.api.RetrofitInstance
import com.example.kolosovfintech.databinding.ActivityMainBinding
import com.example.kolosovfintech.model.Data
import com.example.kolosovfintech.model.Post
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private val binding by viewBinding(ActivityMainBinding::bind)

    private var gifsList: ArrayList<Post>? = arrayListOf()
    private var positionCount = 1
   // private var typeId: Int? = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding.rebootButton.isVisible = false
        binding.rebootButton.isEnabled = false

        setupViewModel()
        initListeners()
        getDefiniteGif()
    }

    private fun initListeners() {

        binding.nextButton.setOnClickListener {
            if (positionCount == gifsList?.size) {
                binding.gifImg.setImageResource(0)
                getDefiniteGif()
                positionCount++
            } else {
                setGif(gifsList?.get(positionCount)?.gifURL)
                setDescription(gifsList?.get(positionCount)?.description)
                positionCount++
            }
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
            binding.rebootButton.isVisible = false
            binding.rebootButton.isEnabled = false
            if (gifsList?.size == 0 || gifsList?.size == positionCount-1) {
                getDefiniteGif()
            } else {
                setGif(gifsList?.get(positionCount - 1)?.gifURL)
            }
        }

        binding.latestButton.setOnClickListener {
            binding.gifImg.setImageResource(0)
            gifsList?.clear()
            Id.typeId = 1
            this.recreate()
        }

        binding.hotButton.setOnClickListener {
            binding.gifImg.setImageResource(0)
            gifsList?.clear()
            Id.typeId = 3
            this.recreate()
        }

        binding.topButton.setOnClickListener {
            binding.gifImg.setImageResource(0)
            gifsList?.clear()
            Id.typeId = 2
            this.recreate()
        }
    }

    private fun getDefiniteGif() {
        when (Id.typeId) {
            1 -> getGif("latest", (0..2600).random())
            2 -> getGif("top", (0..2300).random())
            3 -> getGif("hot", (0..2600).random())
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            MainViewModelFactory(ApiHelper(RetrofitInstance.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun setGif(URL: String?) {

        binding.loadingBar.visibility = View.VISIBLE
        binding.gifCard.visibility = View.INVISIBLE

        Glide.with(this@MainActivity)
            .asGif()
            .load(URL)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.gifCard.visibility = View.VISIBLE
                    binding.descriptionText.text = "Ошибка загрузки гифки"
                    binding.rebootButton.isVisible = true
                    binding.rebootButton.isEnabled = true
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.gifCard.visibility = View.VISIBLE
                    binding.loadingBar.visibility = View.INVISIBLE
                    binding.rebootButton.isVisible = false
                    binding.rebootButton.isEnabled = false
                    return false
                }
            })
            .error(R.drawable.ic_error_image)
            .fallback(R.drawable.ic_error_image)
            .fitCenter()
            .into(binding.gifImg)
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

                        post?.dataModelList.let { it1 ->
                            if (it1 != null) {
                                gifsList?.addAll(it1)
                            }
                        }

                        if (gifsList?.size == 0) {
                            binding.gifCard.visibility = View.VISIBLE
                            binding.rebootButton.isVisible = true
                            binding.gifImg.setImageResource(R.drawable.ic_error_image)
                        }
                        setGif(post!!.dataModelList[0].gifURL)
                        setDescription(post.dataModelList[0].description)
                    }
                    Status.ERROR -> {
                        binding.rebootButton.isVisible = true
                        binding.nextButton.isEnabled = false
                        binding.backButton.isEnabled = false
                        binding.gifCard.visibility = View.VISIBLE
                        binding.gifImg.setImageResource(R.drawable.ic_error_image)
                        binding.descriptionText.text = "Ошибка подкличения к серверу"
                        binding.rebootButton.isEnabled = true
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        binding.loadingBar.visibility = View.VISIBLE
                        binding.gifCard.visibility = View.INVISIBLE
                        binding.nextButton.isEnabled = false
                        binding.backButton.isEnabled = false
                    }
                }
            }
        })
    }
}

object Id{
    var typeId: Int? = 1
}
