package com.example.beekeeper.domain.model.analytics

data class BeehiveAnalytics(
    val id: Int,
    val weightData: List<Double>,
    val temperatureData: List<Double>,
    val saveDateTime: Long
)