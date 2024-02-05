package com.example.shutaffim.Model

data class Request(
    val userId: String,
    val postId: String,
    val isApproved: Boolean = false,
    val message: String =""
)
