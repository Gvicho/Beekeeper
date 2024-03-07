package com.example.beekeeper.data.source.remote.internet.mappers

import com.example.beekeeper.data.source.remote.internet.model.OwnerDto
import com.example.beekeeper.domain.model.farms.Owner

fun OwnerDto.toDomain(): Owner {
    return Owner(
        id = id,
        name = name,
        numberOfFarms = numberOfFarms
    )
}