package com.example.beekeeper.presenter.state.auth.register

import com.example.beekeeper.presenter.model.auth.register.UserAuthenticator

data class RegisterState(
    val isLoading:Boolean = false,
    val userAuthenticator: UserAuthenticator? = null,
    val errorMessage:String? = null
)