package com.socialpub.rahul.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Notif(


    // USENAME ACTION on POST
    //username liked your post
    //username commented on your post
    //username followed you post
    //username unfollowed post
    //0->like , 1->commented , 2->followed, 3-> unfollowed

    @SerializedName("postId")
    @Expose
    var actionOnPostId: String = "",

    @SerializedName("uid")
    @Expose
    var actionByuid: String = "",

    @SerializedName("username")
    @Expose
    var actionByUsername: String = "",

    @SerializedName("username")
    @Expose
    var action: Int = 0,

    @SerializedName("userAvatar")
    @Expose
    var actionByUserAvatar: String = "",

    @SerializedName("timestamp")
    @Expose
    var timestamp: Long = System.currentTimeMillis()
)