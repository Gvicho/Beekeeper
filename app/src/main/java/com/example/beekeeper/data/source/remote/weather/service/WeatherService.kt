package com.example.beekeeper.data.source.remote.weather.service

import com.example.beekeeper.data.source.remote.weather.model.WeatherInfoDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = "1b161cccf87ffff6e0627209da731849",
        @Query("units") units: String? = null,
        @Query("lang") lang: String? = null
    ): Response<WeatherInfoDto>
}