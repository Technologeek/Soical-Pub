package com.socialpub.rahul.data.model

import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("postId")
    @Expose
    var postId: String = "",
    @SerializedName("uid")
    @Expose
    var uid: String = "",
    @SerializedName("username")
    @Expose
    var username: String = "",
    @SerializedName("userAvatar")
    @Expose
    var userAvatar: String = "",

    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String = "",

    @get:Exclude var imagePath: String = "",

    @SerializedName("caption")
    @Expose
    var caption: String = "",

    @SerializedName("location")
    @Expose
    var location: String = "",

    @SerializedName("comments")
    @Expose
    var comments: List<Comment> = emptyList(),

    @SerializedName("likedBy")
    @Expose
    var likedBy: List<Like> = emptyList(),

    @SerializedName("likeCount")
    @Expose
    var likeCount: Long = likedBy.size.toLong(),

    @SerializedName("commentCount")
    @Expose
    var commentCount: Long = comments.size.toLong(),

    @SerializedName("timestamp")
    @Expose
    var timestamp: Long = System.currentTimeMillis()
)