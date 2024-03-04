package com.example.beekeeper.presenter.state

data class LoginUiState(
    val isLoading :Boolean = false,
    val accessToken :String? = null,
    val errorMessage :String? = null
) {
}