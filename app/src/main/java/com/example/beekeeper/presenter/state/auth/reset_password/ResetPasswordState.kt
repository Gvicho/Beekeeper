package com.example.beekeeper.presenter.state.auth.reset_password

data class ResetPasswordState(
    val isLoading:Boolean = false,
    val errorMessage:String? = null
) {
}