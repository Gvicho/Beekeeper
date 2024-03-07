package com.example.beekeeper.data.source.remote.internet.model

import android.net.Uri

data class DamageReportDto(
    val id: String,
    val location: String,
    val damageDescription: String,
    val damageLevelIndicator: String,
    val dateUploaded: Long,
    val damageReason: String,
    val imageUris: List<String>
)