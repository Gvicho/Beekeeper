package com.example.beekeeper.data.source.remote.internet.model.farm

import com.squareup.moshi.Json

data class OwnerDto(
    val id: Int,
    val name: String,
    @Json(name = "number_of_farms")
    val numberOfFarms: Int
)