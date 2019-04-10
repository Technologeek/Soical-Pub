package com.socialpub.rahul.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class Like(
    @SerializedName("uid")
    @Expose
    val uid: String = "",
    @SerializedName("username")
    @Expose
    val username: String = "",
    @SerializedName("userAvatar")
    @Expose
    val userAvatar: String = ""
)