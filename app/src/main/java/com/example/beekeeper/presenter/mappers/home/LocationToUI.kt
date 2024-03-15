package com.example.beekeeper.presenter.mappers.home

import com.example.beekeeper.domain.model.farms.Location
import com.example.beekeeper.presenter.model.home.LocationUi

fun Location.toUI(): LocationUi {
    return LocationUi(
        latitude = latitude,
        longitude = longitude,
        locationName = locationName
    )
}