package com.example.beekeeper.presenter.model.damaged_beehives

import android.net.Uri
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

data class DamageReportUI(
    val id: Int,
    val damageDescription: String,
    val damageLevelIndicator: Int,
    val dateUploaded: String,
    val imageUris: List<Uri>
)