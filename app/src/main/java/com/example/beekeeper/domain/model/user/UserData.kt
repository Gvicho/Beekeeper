package com.example.beekeeper.domain.model.user

data class UserData(
    var token: String,
    var email: String,
    var name: String,
    val lastName: String,
    val image: String? = null
)