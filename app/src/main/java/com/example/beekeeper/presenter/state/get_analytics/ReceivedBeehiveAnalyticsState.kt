package com.example.beekeeper.presenter.state.get_analytics

data class ReceivedBeehiveAnalyticsState(
    val isLoading :Boolean = false,
    val receivedBeehiveAnalytics : Unit? = null,
    val errorMessage :String? = null
)