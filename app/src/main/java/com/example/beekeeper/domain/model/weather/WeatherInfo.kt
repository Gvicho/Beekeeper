package com.example.beekeeper.domain.model.weather

import com.squareup.moshi.Json

data class WeatherInfo(
    val main: MainInfo,
    val weather: List<Weather>
)

data class MainInfo(
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    val seaLevel: Int? = null, // Optional
    val groundLevel: Int? = null // Optional
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
