package com.example.beekeeper.presenter.mappers.home

import com.example.beekeeper.domain.model.farms.Owner
import com.example.beekeeper.presenter.model.home.OwnerUi

fun Owner.toUI(): OwnerUi {
    return OwnerUi(
        id = id,
        name = name,
        numberOfFarms = numberOfFarms
    )
}