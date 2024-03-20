package com.example.beekeeper.data.source.remote.internet.model.user

data class UserDataDto(
    val email: String,
    val userName: String,
    val imageUrl: String? = null
)
