package com.example.beekeeper.presenter.model.home.details

data class FarmDetailsItemWrapper(
    val id:Int,
    val itemType:ItemType,
    val ownerDetailsUi: OwnerDetailsUi? = null,
    val header:FarmDetailsHeaderUi? = null,
    val imagesPager: List<String>? = null,
    val beehiveNumberChartUI: FarmDetailsBeehiveNumberChartUI? = null
) {
    enum class ItemType{
        HEADER, // id,name,location
        IMAGE_PAGER, // for images
        OWNER_DETAILS,// owner info
        BEEHIVE_NUMBER_CHART // for chart, current number
    }
}