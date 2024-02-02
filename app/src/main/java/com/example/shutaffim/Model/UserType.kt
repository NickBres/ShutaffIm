package com.example.shutaffim.Model

sealed class UserType(val type: String) {
    object Publisher : UserType("publisher")
    object Consumer : UserType("consumer")
}
