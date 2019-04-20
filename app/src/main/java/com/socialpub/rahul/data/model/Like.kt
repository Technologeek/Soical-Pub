package com.socialpub.rahul.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Like(
    @SerializedName("uid")
    @Expose
    val uid: String = "",
    @SerializedName("username")
    @Expose
    val username: String = "",
    @SerializedName("useremail")
    @Expose
    val userEmail: String = "",
    @SerializedName("userAvatar")
    @Expose
    val userAvatar: String = ""
)