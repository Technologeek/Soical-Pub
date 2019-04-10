package com.socialpub.rahul.data.model

import java.util.*

data class Comment(
    val uid: String = "",
    val username: String = "",
    val text: String = "",
    val date: Date
)