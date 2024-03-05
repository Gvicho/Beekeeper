package com.example.beekeeper.presenter.model.home

data class FarmUi(
    val id: Int,
    val beeHiveNumber: Int,
    val farmName: String,
    val location: LocationUi,
    val owner: OwnerUi,
    val images: List<String>
) {
}