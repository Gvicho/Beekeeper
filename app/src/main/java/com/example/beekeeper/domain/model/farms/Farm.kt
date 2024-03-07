package com.example.beekeeper.domain.model.farms


data class Farm(
    val id: Int,
    val beeHiveNumber: Int,
    val farmName: String,
    val location: Location,
    val owner: Owner,
    val images: List<String>
) {
}