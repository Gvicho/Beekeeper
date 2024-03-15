package com.example.beekeeper.data.source.remote.internet.model.farm.details

import com.example.beekeeper.data.source.remote.internet.model.farm.LocationDto
import com.squareup.moshi.Json

data class FarmDetailsDto(
    val id: Int,
    @Json(name = "bee_hive_number") val beeHiveNumber: Int,
    @Json(name = "last_year_growth")val lastYearGrowth: List<Int>,
    @Json(name = "farm_name") val farmName: String,
    val location: LocationDto,
    @Json(name = "owner_details") val ownerDetails: OwnerDetailsDto,
    val images: List<String>
) {
}