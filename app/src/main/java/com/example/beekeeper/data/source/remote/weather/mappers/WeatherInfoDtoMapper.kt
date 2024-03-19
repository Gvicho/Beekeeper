package com.example.beekeeper.data.source.remote.weather.mappers

import com.example.beekeeper.data.source.remote.weather.model.WeatherDto
import com.example.beekeeper.data.source.remote.weather.model.WeatherInfoDto
import com.example.beekeeper.domain.model.weather.MainInfo
import com.example.beekeeper.domain.model.weather.Weather
import com.example.beekeeper.domain.model.weather.WeatherInfo


fun WeatherInfoDto.toDomain() = WeatherInfo(main = MainInfo(
    temp = main.temp,
    feelsLike = main.feelsLike,
    tempMin = main.tempMin,
    tempMax = main.tempMax,
    pressure = main.pressure,
    humidity = main.humidity,
    seaLevel = main.seaLevel,
    groundLevel = main.groundLevel
), weather = weather.map {
    it.toDomain()

})

fun WeatherDto.toDomain() = Weather(id = id, main = main, description = description, icon = icon)