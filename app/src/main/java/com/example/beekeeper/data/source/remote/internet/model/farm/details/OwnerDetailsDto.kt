package com.example.beekeeper.data.source.remote.internet.model.farm.details

import com.squareup.moshi.Json

data class OwnerDetailsDto(
    val id: Int,
    val name: String,
    val profile:String,
    @Json(name = "number_of_farms") val numberOfFarms: Int,
    val email: String,
    val phone: String
) {
}