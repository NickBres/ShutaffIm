package com.example.shutaffim.Model

data class Post(
    val id: String = "",
    var date: String = "",
    val city: String,
    val street: String,
    val house_num: Int,
    val curr_roommates: Int,
    val max_roommates: Int,
    val price: Int,
    val tags: List<String> = listOf(),
    val about: String = "",
    val pic1: String = "",
    val pic2: String = "",
    val pic3: String = "",
    val pic4: String = "",
    val userId: String
)
