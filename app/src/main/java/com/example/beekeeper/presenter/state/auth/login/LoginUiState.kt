package com.example.beekeeper.presenter.state.auth.login

data class LoginUiState(
    val isLoading :Boolean = false,
    val accessToken :String? = null,
    val errorMessage :String? = null
) {
}