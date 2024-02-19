package com.example.shutaffim.Model

data class Topic(
    val id: String = "",
    val title: String,
    val description: String,
    val email: String,
    var date: Long,
    val userName: String = "",
    val commentsCount: Int = 0
)
