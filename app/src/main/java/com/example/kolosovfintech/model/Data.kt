package com.example.kolosovfintech.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(

    @SerializedName("result")
    @Expose
    var dataModelList: List<Post> ,

    @SerializedName("totalCount")
    @Expose
    val totalCount : Int

)
