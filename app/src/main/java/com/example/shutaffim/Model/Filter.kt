package com.example.shutaffim.Model

data class Filter(
    val id: String = "",
    val city: String = "",
    val street: String = "",
    val minPrice: Int = 0,
    val maxPrice: Int = 10000,
    val tags: String = "",
    val email: String = ""
    )
