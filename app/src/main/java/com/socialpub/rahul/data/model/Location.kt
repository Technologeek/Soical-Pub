package com.socialpub.rahul.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class Location(
    @SerializedName("lat")
    @Expose
    val lat: Double = 0.0,
    @SerializedName("long")
    @Expose
    val long: Double = 0.0,
    @SerializedName("name")
    @Expose
    val name: String = ""
)