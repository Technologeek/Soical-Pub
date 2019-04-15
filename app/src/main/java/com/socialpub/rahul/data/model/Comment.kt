package com.socialpub.rahul.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("uid")
    @Expose
    val uid: String = "",
    @SerializedName("username")
    @Expose
    val userAvatar: String = "",
    @SerializedName("text")
    @Expose
    val text: String = ""
)