package com.example.beekeeper.domain.repository.weather

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.weather.WeatherInfo
import kotlinx.coroutines.flow.Flow

interface GetWeatherRepository {

    fun getWeather(lat: Double, lon: Double): Flow<Resource<WeatherInfo>>
}