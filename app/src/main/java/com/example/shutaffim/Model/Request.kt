package com.example.shutaffim.Model

data class Request(
    val userId: String,
    val postId: String,
    val date :Long=0L,
    val message: String =""
)
