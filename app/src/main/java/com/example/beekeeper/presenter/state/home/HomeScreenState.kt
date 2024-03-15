package com.example.beekeeper.presenter.state.home

import com.example.beekeeper.presenter.model.home.FarmUi

data class HomeScreenState(
    val isLoading :Boolean = false,
    val farmList :List<FarmUi>? = null,
    val errorMessage :String? = null
) {
}