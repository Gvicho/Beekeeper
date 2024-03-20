package com.example.beekeeper.presenter.model.user

data class UserDataUI(
    var email: String,
    var name: String,
    val lastName: String,
    val image: String? = null
)