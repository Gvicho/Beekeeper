package com.example.beekeeper.presenter.model.beehive_analytics

data class BeehiveAnalyticsUI(
    val id: Int,
    val weightData: List<Float>,
    val temperatureData: List<Float>,
    val saveDateTime: Long
)