package com.example.beekeeper.data.source.remote.internet.model

import android.net.Uri

data class DamageReportDto(
    val id: String = "",
    val damageDescription: String = "",
    val damageLevelIndicator: Int = 0,
    val dateUploaded: String = "",
    val imageUris: List<String> = listOf()
)