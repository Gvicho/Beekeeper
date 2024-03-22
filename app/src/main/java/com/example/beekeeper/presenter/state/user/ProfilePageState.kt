package com.example.beekeeper.presenter.state.user

import com.example.beekeeper.presenter.model.user.UserDataUI

data class ProfilePageState(
    val isLoading:Boolean = false,
    val userDataUI: UserDataUI? = null,
    val errorMessage:String? = null
)