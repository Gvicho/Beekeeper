package com.example.beekeeper.domain.model.damaged_beehives

import android.net.Uri

data class DamageReport(
    val id: String,
    val damageDescription: String,
    val damageLevelIndicator: Int,
    val dateUploaded: String,
    val imageUris: List<Uri>
)