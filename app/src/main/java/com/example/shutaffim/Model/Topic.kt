package com.example.shutaffim.Model

data class Topic(
    val id: String,
    val title: String,
    val description: String,
    val email: String,
    val date: Long,
    val userName: String = ""
)
