package com.example.beekeeper.domain.repository.weather

import com.example.beekeeper.domain.common.Result
import com.example.beekeeper.domain.error_handling.DataError
import com.example.beekeeper.domain.model.weather.WeatherInfo
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getWeather(lat: Double, lon: Double): Flow<Result<WeatherInfo, DataError.NetworkError>>
}