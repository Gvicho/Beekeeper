package com.example.beekeeper.presenter.mappers.weather

import com.example.beekeeper.domain.model.weather.MainInfo
import com.example.beekeeper.domain.model.weather.Weather
import com.example.beekeeper.domain.model.weather.WeatherInfo
import com.example.beekeeper.presenter.model.home.details.FarmDetailsItemWrapper
import com.example.beekeeper.presenter.model.weather.MainInfoUI
import com.example.beekeeper.presenter.model.weather.WeatherInfoUI
import com.example.beekeeper.presenter.model.weather.WeatherUI

fun WeatherInfo.toPresentation() = FarmDetailsItemWrapper(
    id = 1,
    itemType = FarmDetailsItemWrapper.ItemType.WEATHER_INFO,
    ownerDetailsUi = null,
    header = null,
    imagesPager = null,
    beehiveNumberChartUI = null,
    weatherInfoUI = WeatherInfoUI(
        main = main.toPresentation(),
        weather = weather.map {
            it.toPresentation()
        }
    )
)

fun Weather.toPresentation() = WeatherUI(
    id = id,
    main = main,
    description = description,
    image = "https://openweathermap.org/img/wn/$icon.png"
)
fun MainInfo.toPresentation() = MainInfoUI(
    temp = getWholePart(temp.toCelsius()), // convert from kelvin to Celsius
    feelsLike = getWholePart(feelsLike.toCelsius()),
    tempMin = getWholePart(tempMin.toCelsius()),
    tempMax = getWholePart(tempMax.toCelsius()),
    pressure = pressure,
    humidity = humidity,
    seaLevel = seaLevel,
    groundLevel = groundLevel
)

fun Double.toCelsius():Double{
    return this - 273.15
}

fun getWholePart(number: Double): Int {
    return number.toInt()
}