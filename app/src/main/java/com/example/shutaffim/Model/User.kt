package com.example.shutaffim.Model

data class User(
    val email: String,
    val fName: String,
    val lName: String,
    val about: String = "",
    val picture: Picture = Picture(),
    val type: String,
    val birthYear: Int,
    val sex: String = ""
)
