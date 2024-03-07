package com.example.beekeeper.domain.model.damaged_beehives

import android.net.Uri

data class DamageReport(
    val id: String,
    val location: String,
    val damageDescription: String,
    val damageLevelIndicator: String,
    val dateUploaded: Long,
    val damageReason: String,
    val imageUris: List<Uri>
)