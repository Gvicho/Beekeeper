package com.example.beekeeper.presenter.state

import com.example.beekeeper.presenter.model.UserAuthenticator

data class RegisterState(
    val isLoading:Boolean = false,
    val userAuthenticator: UserAuthenticator? = null,
    val errorMessage:String? = null
)