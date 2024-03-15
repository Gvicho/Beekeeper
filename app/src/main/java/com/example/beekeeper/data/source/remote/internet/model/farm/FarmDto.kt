package com.example.beekeeper.data.source.remote.internet.model.farm

import com.squareup.moshi.Json

data class FarmDto(
    val id: Int,
    @Json(name = "bee_hive_number")
    val beeHiveNumber: Int,
    @Json(name = "farm_name")
    val farmName: String,
    val location: LocationDto,
    val owner: OwnerDto,
    val images: List<String>
)