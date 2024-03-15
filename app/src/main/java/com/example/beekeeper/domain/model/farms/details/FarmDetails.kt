package com.example.beekeeper.domain.model.farms.details

import com.example.beekeeper.domain.model.farms.Location

data class FarmDetails(
    val id: Int,
    val beeHiveNumber: Int,
    val lastYearGrowth: List<Int>,
    val farmName: String,
    val location: Location,
    val ownerDetails: OwnerDetails,
    val images: List<String>
) {
}