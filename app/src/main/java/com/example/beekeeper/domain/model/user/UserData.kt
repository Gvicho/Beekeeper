package com.example.beekeeper.domain.model.user

data class UserData(
    val email: String,
    val userName: String,
    val imageUrl: String? = null
)