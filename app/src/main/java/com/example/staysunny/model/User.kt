package com.example.staysunny.model

import java.util.Date

data class User(
    val id: String = "",
    val name: String = "",
    val lastName: String = "",
    val userName: String = "",
    val bornDate: Date = Date()
)