package com.example.beekeeper.data.source.remote.weather.model

import com.squareup.moshi.Json

data class WeatherInfoDto(
    @Json(name = "main") val main: MainInfoDto,
    @Json(name = "weather") val weather: List<WeatherDto>
)

data class MainInfoDto(
    @Json(name = "temp") val temp: Double,
    @Json(name = "feels_like") val feelsLike: Double,
    @Json(name = "temp_min") val tempMin: Double,
    @Json(name = "temp_max") val tempMax: Double,
    @Json(name = "pressure") val pressure: Int,
    @Json(name = "humidity") val humidity: Int,
    @Json(name = "sea_level") val seaLevel: Int? = null, // Optional
    @Json(name = "grnd_level") val groundLevel: Int? = null // Optional
)

data class WeatherDto(
    @Json(name = "id") val id: Int,
    @Json(name = "main") val main: String,
    @Json(name = "description") val description: String,
    @Json(name = "icon") val icon: String
)
