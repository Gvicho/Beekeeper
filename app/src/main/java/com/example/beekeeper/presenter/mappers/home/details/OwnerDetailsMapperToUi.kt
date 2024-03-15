package com.example.beekeeper.presenter.mappers.home.details

import com.example.beekeeper.domain.model.farms.details.OwnerDetails
import com.example.beekeeper.presenter.model.home.details.OwnerDetailsUi


fun OwnerDetails.toUi():OwnerDetailsUi{
    return OwnerDetailsUi(
        id = id,
        name = name,
        numberOfFarms = numberOfFarms,
        email = email,
        phone = phone
    )
}