package com.example.beekeeper.presenter.mappers.home.details

import com.example.beekeeper.domain.model.farms.details.FarmDetails
import com.example.beekeeper.presenter.mappers.home.toUI
import com.example.beekeeper.presenter.model.home.details.FarmDetailsBeehiveNumberChartUI
import com.example.beekeeper.presenter.model.home.details.FarmDetailsHeaderUi
import com.example.beekeeper.presenter.model.home.details.FarmDetailsItemWrapper
import com.example.beekeeper.presenter.model.home.details.OwnerDetailsUi


fun FarmDetails.toUi():List<FarmDetailsItemWrapper>{
    return listOf(
        FarmDetailsItemWrapper(
            id = 0,
            itemType = FarmDetailsItemWrapper.ItemType.HEADER,
            ownerDetailsUi = null,
            header = this.toHeader(),
            imagesPager = null,
            beehiveNumberChartUI = null
        ),
        FarmDetailsItemWrapper(
            id = 1,
            itemType = FarmDetailsItemWrapper.ItemType.IMAGE_PAGER,
            ownerDetailsUi = null,
            header = null,
            imagesPager = this.toImages(),
            beehiveNumberChartUI = null
        ),
        FarmDetailsItemWrapper(
            id = 2,
            itemType = FarmDetailsItemWrapper.ItemType.BEEHIVE_NUMBER_CHART,
            ownerDetailsUi = null,
            header = null,
            imagesPager = null,
            beehiveNumberChartUI = this.toBeehiveNumberChart()
        ),
        FarmDetailsItemWrapper(
            id = 3,
            itemType = FarmDetailsItemWrapper.ItemType.HEADER,
            ownerDetailsUi = this.toOwnerDetailsUi(),
            header = null,
            imagesPager = null,
            beehiveNumberChartUI = null
        )
    )
}

fun FarmDetails.toHeader():FarmDetailsHeaderUi{
    return FarmDetailsHeaderUi(
        id = id,
        name = farmName,
        locationUi = location.toUI()
    )
}

fun FarmDetails.toImages():List<String>{
    return images
}

fun FarmDetails.toBeehiveNumberChart():FarmDetailsBeehiveNumberChartUI{
    return FarmDetailsBeehiveNumberChartUI(
        currentBeehiveNum = beeHiveNumber,
        lastYearsGrowth = lastYearGrowth

    )
}

fun FarmDetails.toOwnerDetailsUi():OwnerDetailsUi{
    return OwnerDetailsUi(
        id = ownerDetails.id,
        name = ownerDetails.name,
        numberOfFarms = ownerDetails.numberOfFarms,
        email = ownerDetails.email,
        phone = ownerDetails.phone
    )
}