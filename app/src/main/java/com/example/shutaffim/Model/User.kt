package com.example.shutaffim.Model

import java.net.URL

data class User(
    val email: String,
    val fName : String,
    val lName : String,
    val about : String = "",
    val picture : String = "",
    val type : String ,
    val age: Int,
    val sex: String =""

)
