package com.example.shutaffim.Model

data class InterestedInPost(
    val postId: String,
    val userEmail: String,
    val isApproved: Boolean = false
)
