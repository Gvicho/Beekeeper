package com.example.beekeeper.data.source.remote.internet.model.user

data class UserDataDto(
    var token: String = "",
    var email: String = "",
    var name: String = "",
    var lastName: String = "",
    var image: String? = null
)
