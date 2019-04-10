package com.socialpub.rahul.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.FieldValue

data class UserPost(
    val userPost: HashMap<String, Post>?
)

data class LikedPost(
    val userPost: HashMap<String, Post>?
)

data class GlobalPost(
    val globalPost: HashMap<String, Post>?
)

data class Post(
    val postId: String = "",
    val uid: String = "",
    val username: String = "",
    val userProfilePic: String = "",
    val imageUrl: String = "",
    @get:Exclude val imagePath: String,
    val caption: String = "",
    val location: String = "",
    val comments: List<Comment> = emptyList(),
    val likedBy: List<String> = emptyList(),
    val likeCount: Long = likedBy.size.toLong(),
    val commentCount: Long = comments.size.toLong(),
    val date: FieldValue = FieldValue.serverTimestamp()
)