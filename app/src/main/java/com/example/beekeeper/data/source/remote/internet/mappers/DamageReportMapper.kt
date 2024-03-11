package com.example.beekeeper.data.source.remote.internet.mappers

import androidx.core.net.toUri
import com.example.beekeeper.data.source.remote.internet.model.DamageReportDto
import com.example.beekeeper.domain.model.damaged_beehives.DamageReport


fun DamageReport.toData() = DamageReportDto(
    id = id,
    damageDescription = damageDescription,
    damageLevelIndicator = damageLevelIndicator,
    dateUploaded = dateUploaded,
    imageUris = imageUris.map { it.toString() }

)

fun DamageReportDto.toDomain() = DamageReport(
    id = id,
    damageDescription = damageDescription,
    damageLevelIndicator = damageLevelIndicator,
    dateUploaded = dateUploaded,
    imageUris = imageUris.map {
        it.toUri()
    }
)
