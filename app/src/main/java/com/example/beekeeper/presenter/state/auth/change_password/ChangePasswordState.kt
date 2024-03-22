package com.example.beekeeper.presenter.state.auth.change_password

data class ChangePasswordState(
    val isLoading:Boolean = false,
    val passwordChangedSuccessfully:Unit? = null,
    val errorMessage:String? = null
) {
}