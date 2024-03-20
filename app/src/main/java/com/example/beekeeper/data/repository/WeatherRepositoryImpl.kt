package com.example.beekeeper.data.repository

import com.example.beekeeper.data.common.HandleResponse
import com.example.beekeeper.data.common.mapResource
import com.example.beekeeper.data.source.remote.weather.mappers.toDomain
import com.example.beekeeper.data.source.remote.weather.service.WeatherService
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.weather.WeatherInfo
import com.example.beekeeper.domain.repository.weather.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherService,
    private val handleResponse: HandleResponse
) :WeatherRepository {
    override fun getWeather(lat: Double, lon: Double): Flow<Resource<WeatherInfo>> {
        return handleResponse.safeApiCallRetrofit {
            weatherService.getWeather(lat = lat, lon = lon)
        }.mapResource {weatherInfo ->
            weatherInfo.toDomain()
        }

    }
}

