package com.socialpub.rahul.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class User(

    @SerializedName("name")
    @Expose
    var name: String = "",

    @SerializedName("username")
    @Expose
    var username: String = "",

    @SerializedName("email")
    @Expose
    var email: String = "",

    @SerializedName("follows")
    @Expose
    var following: List<String> = emptyList(),

    @SerializedName("followers")
    @Expose
    var followers: List<String> = emptyList(),

    @SerializedName("bio")
    @Expose
    var bio: String? = null,

    @SerializedName("location")
    @Expose
    var location: String? = null,

    @SerializedName("avatar")
    @Expose
    var avatar: String? = null,

    @SerializedName("uid")
    @Expose
    var uid: String = ""
)