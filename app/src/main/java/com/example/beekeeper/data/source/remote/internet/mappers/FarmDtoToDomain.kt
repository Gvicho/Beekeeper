package com.example.beekeeper.data.source.remote.internet.mappers

import com.example.beekeeper.data.source.remote.internet.model.FarmDto
import com.example.beekeeper.domain.model.Farm


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