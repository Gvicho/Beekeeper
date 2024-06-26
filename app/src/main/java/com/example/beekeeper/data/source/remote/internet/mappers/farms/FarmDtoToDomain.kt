package com.example.beekeeper.data.source.remote.internet.mappers.farms

import com.example.beekeeper.data.source.remote.internet.model.farm.FarmDto
import com.example.beekeeper.domain.model.farms.Farm


fun FarmDto.toDomain(): Farm {
    return Farm(
        id = id,
        beeHiveNumber = beeHiveNumber,
        farmName = farmName,
        location = location.toDomain(),
        owner = owner.toDomain(),
        images = images
    )
}