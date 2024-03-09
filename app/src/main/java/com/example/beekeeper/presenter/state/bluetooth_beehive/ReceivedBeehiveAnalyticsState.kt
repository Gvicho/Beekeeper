package com.example.beekeeper.presenter.state.bluetooth_beehive

import com.example.beekeeper.domain.model.BeehiveAnalytics

data class ReceivedBeehiveAnalyticsState(
    val isLoading :Boolean = false,
    val receivedBeehiveAnalytics : BeehiveAnalytics? = null,
    val errorMessage :String? = null
) {
}