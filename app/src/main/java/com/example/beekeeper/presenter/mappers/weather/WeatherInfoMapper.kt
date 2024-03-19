package com.example.beekeeper.presenter.mappers.weather

import com.example.beekeeper.domain.model.weather.Weather
import com.example.beekeeper.domain.model.weather.WeatherInfo
import com.example.beekeeper.presenter.model.weather.MainInfoUI
import com.example.beekeeper.presenter.model.weather.WeatherInfoUI
import com.example.beekeeper.presenter.model.weather.WeatherUI

fun WeatherInfo.toPresentation() = WeatherInfoUI(main = MainInfoUI(
    temp = main.temp,
    feelsLike = main.feelsLike,
    tempMin = main.tempMin,
    tempMax = main.tempMax,
    pressure = main.pressure,
    humidity = main.humidity,
    seaLevel = main.seaLevel,
    groundLevel = main.groundLevel
), weather = weather.map {
    it.toPresentation()

})

fun Weather.toPresentation() = WeatherUI(id = id, main = main, description = description, image = "https://openweathermap.org/img/wn/$icon.png")