package com.socialpub.rahul.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("username")
    @Expose
    val username: String = "",
    @SerializedName("email")
    @Expose
    val email: String = "",
    @SerializedName("follows")
    @Expose
    val follows: Map<String, Boolean> = emptyMap(),
    @SerializedName("followers")
    @Expose
    val followers: Map<String, Boolean> = emptyMap(),
    @SerializedName("bio")
    @Expose
    val bio: String? = null,
    @SerializedName("avatar")
    @Expose
    val avatar: String? = null,
    @SerializedName("uid")
    @Expose
    val uid: String = ""
)