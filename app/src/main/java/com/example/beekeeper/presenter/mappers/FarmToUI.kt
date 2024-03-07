package com.example.beekeeper.presenter.mappers

import com.example.beekeeper.domain.model.farms.Farm
import com.example.beekeeper.presenter.model.home.FarmUi

fun Farm.toUI(): FarmUi {
    return FarmUi(
        id = id,
        beeHiveNumber = beeHiveNumber,
        farmName = farmName,
        location = location.toUI(),
        owner = owner.toUI(),
        images = images
    )
}