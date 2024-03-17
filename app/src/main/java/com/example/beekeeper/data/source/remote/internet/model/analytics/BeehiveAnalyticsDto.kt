package com.example.beekeeper.data.source.remote.internet.model.analytics

data class BeehiveAnalyticsDto(
    val id: Int,
    val weightData: List<Double>,
    val temperatureData: List<Double>,
    val saveDateTime: Long
)