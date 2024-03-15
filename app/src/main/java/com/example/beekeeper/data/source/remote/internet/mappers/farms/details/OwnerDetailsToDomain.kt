package com.example.beekeeper.data.source.remote.internet.mappers.farms.details

import com.example.beekeeper.data.source.remote.internet.model.farm.details.OwnerDetailsDto
import com.example.beekeeper.domain.model.farms.details.OwnerDetails

fun OwnerDetailsDto.toDomain(): OwnerDetails {
    return OwnerDetails(
        id = id,
        name = name,
        numberOfFarms = numberOfFarms,
        email = email,
        phone = phone
    )
}