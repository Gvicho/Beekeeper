package com.example.beekeeper.domain.model

data class BeehiveAnalytics(
    val id: Int,
    val weightData: List<Double>,
    val temperatureData: List<Double>
) {
}