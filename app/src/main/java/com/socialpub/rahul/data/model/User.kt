package com.socialpub.rahul.data.model


data class User(
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val follows: Map<String, Boolean> = emptyMap(),
    val followers: Map<String, Boolean> = emptyMap(),
    val bio: String? = null,
    val photo: String? = null,
    val likedPost: LikedPost? = null,
    val uid: String = ""
)