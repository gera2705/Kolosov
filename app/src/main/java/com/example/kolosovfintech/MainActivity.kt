package com.example.kolosovfintech

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.kolosovfintech.repository.Repository
import com.example.rtest.MainViewModel
import com.example.rtest.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var gif: ImageView

    private lateinit var button: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gif = findViewById(R.id.gif_img)
        button = findViewById(R.id.next_button)

        val repo = Repository()
        val viewModelFactory = MainViewModelFactory(repo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getPost()



        get()


        button.setOnClickListener {
            viewModel.getPost()
            viewModel.meResponse.observe(this , Observer {
                    responce ->
                Log.d("Resp" , responce.body().toString())
                if(responce.isSuccessful){
                    Log.d("Resp" , responce.body()?.description.toString())
                    putGif(responce.body()?.gifURL.toString() , "")
                }else{
                    Log.d("Resp", responce.errorBody().toString())
                }

            })
        }
    }

    fun get(){
        viewModel.meResponse.observe(this , Observer {
                responce ->

            if(responce.isSuccessful){
                Log.d("Resp" , responce.body()?.description.toString())
                putGif(responce.body()?.gifURL.toString() , "")
            }else{
                Log.d("Resp", responce.errorBody().toString())
            }

        })
    }


    private fun putGif(URL: String, description: String) {

        Glide.with(this@MainActivity)
            .asGif()
            .load(URL)
            .placeholder(R.drawable.progress_bar)
            .error(R.drawable.ic_error_image)
            .fallback(R.drawable.ic_error_image)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(gif)
    }
}