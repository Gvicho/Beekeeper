package com.example.beekeeper.data.source.remote.internet.model

data class DamageReportDto(
    val id: Int = 0,
    val damageDescription: String = "",
    val damageLevelIndicator: Int = 0,
    val dateUploaded: String = "",
    val imageUris: List<String> = listOf()
)