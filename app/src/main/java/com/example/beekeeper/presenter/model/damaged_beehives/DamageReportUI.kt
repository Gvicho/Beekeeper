package com.example.beekeeper.presenter.model.damaged_beehives

import android.net.Uri

data class DamageReportUI(
    val id: String,
    val damageDescription: String,
    val damageLevelIndicator: Int,
    val dateUploaded: String,
    val imageUris: List<Uri>
)