package com.example.beekeeper.presenter.model.weather

import com.example.beekeeper.domain.model.weather.MainInfo

data class WeatherInfoUI(
    val main: MainInfoUI,
    val weather: List<WeatherUI>
)

data class MainInfoUI(
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    val seaLevel: Int? = null, // Optional
    val groundLevel: Int? = null // Optional
)

data class WeatherUI(
    val id: Int,
    val main: String,
    val description: String,
    val image: String
)
