package com.example.beekeeper.presenter.model.weather

data class WeatherInfoUI(
    val main: MainInfoUI,
    val weather: List<WeatherUI>
)

data class MainInfoUI(
    val temp: Int,
    val feelsLike: Int,
    val tempMin: Int,
    val tempMax: Int,
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
