package com.example.beekeeper.data.source.remote.internet.model.damaged_beehives

data class DamageReportDto(
    val id: Int = 0,
    val damageDescription: String = "",
    val damageLevelIndicator: Int = 0,
    val dateUploaded: String = "",
    var imageUris: List<String> = listOf()
)