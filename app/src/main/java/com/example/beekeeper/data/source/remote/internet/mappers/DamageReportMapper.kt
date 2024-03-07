package com.example.beekeeper.data.source.remote.internet.mappers

import com.example.beekeeper.data.source.remote.internet.model.DamageReportDto
import com.example.beekeeper.domain.model.damaged_beehives.DamageReport


fun DamageReport.toData() = DamageReportDto(
    id = id,
    location = location,
    damageDescription = damageDescription,
    damageLevelIndicator = damageLevelIndicator,
    dateUploaded = dateUploaded,
    damageReason = damageReason,
    imageUris = imageUris.map { it.toString() }
)
