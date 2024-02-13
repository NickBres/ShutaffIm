package com.example.shutaffim.Model

data class Comment(
    val id: String,
    val text: String,
    val email: String,
    val date: Long,
    val userName: String = ""
)