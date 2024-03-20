package com.example.beekeeper.domain.usecase.weather

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.weather.WeatherInfo
import com.example.beekeeper.domain.repository.weather.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {

    operator fun invoke(lat: Double, lon: Double): Flow<Resource<WeatherInfo>> =
        weatherRepository.getWeather(lat, lon)
}