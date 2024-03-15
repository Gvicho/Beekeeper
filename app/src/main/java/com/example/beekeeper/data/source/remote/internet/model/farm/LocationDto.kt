package com.example.beekeeper.data.source.remote.internet.model.farm

import com.squareup.moshi.Json

data class LocationDto(
    val latitude: Double,
    val longitude: Double,
    @Json(name = "location_name")
    val locationName: String
)