package com.example.beekeeper.data.source.remote.internet.model.user

data class UserDataDto(
    var email: String,
    var name: String,
    val lastName: String,
    val image: String? = null
)
