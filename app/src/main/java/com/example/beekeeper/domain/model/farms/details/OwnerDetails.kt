package com.example.beekeeper.domain.model.farms.details

data class OwnerDetails(
    val id: Int,
    val name: String,
    val numberOfFarms: Int,
    val email: String,
    val phone: String
) {
}