package com.example.beekeeper.domain.usecase.weather

import com.example.beekeeper.domain.common.Result
import com.example.beekeeper.domain.error_handling.DataError
import com.example.beekeeper.domain.model.weather.WeatherInfo
import com.example.beekeeper.domain.repository.weather.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {

    operator fun invoke(lat: Double, lon: Double): Flow<Result<WeatherInfo, DataError.NetworkError>> =
        weatherRepository.getWeather(lat, lon)
}