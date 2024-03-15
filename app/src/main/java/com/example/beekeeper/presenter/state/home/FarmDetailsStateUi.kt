package com.example.beekeeper.presenter.state.home

import com.example.beekeeper.presenter.model.home.details.FarmDetailsItemWrapper

data class FarmDetailsStateUi(
    val isLoading :Boolean = false,
    val farmDetails :List<FarmDetailsItemWrapper>? = null,
    val errorMessage :String? = null
) {
}