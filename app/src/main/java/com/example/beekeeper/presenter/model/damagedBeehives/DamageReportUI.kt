package com.example.beekeeper.presenter.model.damagedBeehives

import android.net.Uri

data class DamageReportUI(
    val id: String,
    val location: String,
    val damageDescription: String,
    val damageLevelIndicator: String,
    val dateUploaded: Long,
    val damageReason: String,
    val imageUris: List<Uri>
)