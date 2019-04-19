package com.socialpub.rahul.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Notif(


    // USENAME ACTION on POST
    //username liked your post
    //username commented on your post
    //username followed you post
    //username reported your post
    //0->like , 1->commented , 2->followed, 3-> reported

    @SerializedName("actionOnPostId")
    @Expose
    var actionOnPostId: String = "",

    @SerializedName("actionByuid")
    @Expose
    var actionByuid: String = "",

    @SerializedName("actionByUsername")
    @Expose
    var actionByUsername: String = "",

    @SerializedName("action")
    @Expose
    var action: Int = 0,

    @SerializedName("actionByUserAvatar")
    @Expose
    var actionByUserAvatar: String = "",

    @SerializedName("timestamp")
    @Expose
    var timestamp: Long = System.currentTimeMillis()
)