package com.example.beekeeper.data.source.remote.internet.mappers.farms

import com.example.beekeeper.data.source.remote.internet.model.farm.LocationDto
import com.example.beekeeper.domain.model.farms.Location

fun LocationDto.toDomain(): Location {
    return Location(
        latitude = latitude,
        longitude = longitude,
        locationName = locationName
    )
}