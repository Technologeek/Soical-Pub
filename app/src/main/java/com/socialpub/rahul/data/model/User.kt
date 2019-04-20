package com.socialpub.rahul.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class User(

    @SerializedName("username")
    @Expose
    var username: String = "",

    @SerializedName("email")
    @Expose
    var email: String = "",

    @SerializedName("following")
    @Expose
    var following: List<String> = emptyList(),

    @SerializedName("followedBy")
    @Expose
    var followedBy: List<String> = emptyList(),

    @SerializedName("avatar")
    @Expose
    var avatar: String? = null,

    @SerializedName("isVisibletoLocationSearch")
    @Expose
    var isVisibletoLocationSearch: Boolean = true,

    @SerializedName("isVisibletoEmailSearch")
    @Expose
    var isVisibletoEmailSearch: Boolean = true,

    @SerializedName("uid")
    @Expose
    var uid: String = ""
)