package com.example.beekeeper.presenter.state.get_analytics

data class SaveAnalyticsState(
    val isLoading :Boolean = false,
    val saveStatus : Unit? = null,
    val errorMessage :String? = null
)