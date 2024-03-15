package com.example.beekeeper.data.source.remote.internet.mappers.farms.details

import com.example.beekeeper.data.source.remote.internet.mappers.farms.toDomain
import com.example.beekeeper.data.source.remote.internet.model.farm.details.FarmDetailsDto
import com.example.beekeeper.domain.model.farms.details.FarmDetails

fun FarmDetailsDto.toDomain(): FarmDetails {
    return FarmDetails(
        id = id,
        beeHiveNumber = beeHiveNumber,
        lastYearGrowth = lastYearGrowth,
        farmName = farmName,
        location = location.toDomain(),
        ownerDetails =ownerDetails.toDomain(),
        images = images
    )
}