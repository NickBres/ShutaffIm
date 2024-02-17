package com.example.shutaffim.Model

data class Post(
    val id: String = "",
    var date: Long = 0L,
    val city: String,
    val street: String,
    val house_num: Int,
    val curr_roommates: Int,
    val max_roommates: Int,
    val price: Int,
    val tags: List<String> = listOf(),
    val about: String = "",
    val pictures: List<Picture> = listOf(),
    val userId: String
)
