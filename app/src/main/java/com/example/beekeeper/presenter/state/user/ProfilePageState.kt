package com.example.beekeeper.presenter.state.user

data class ProfilePageState(
    val isLoading:Boolean = false,
    val profileInfoSaved:Unit? = null,
    val errorMessage:String? = null
)