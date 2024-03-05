package com.example.beekeeper.presenter.mappers

import com.example.beekeeper.domain.model.Location
import com.example.beekeeper.presenter.model.home.LocationUi

fun Location.toUI(): LocationUi {
    return LocationUi(
        latitude = latitude,
        longitude = longitude,
        locationName = locationName
    )
}