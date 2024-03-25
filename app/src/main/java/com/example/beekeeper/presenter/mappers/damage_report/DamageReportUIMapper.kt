package com.example.beekeeper.presenter.mappers.damage_report

import com.example.beekeeper.domain.model.damaged_beehives.DamageReport
import com.example.beekeeper.presenter.model.damaged_beehives.DamageReportUI


fun DamageReportUI.toDomain() = DamageReport(
    id = id,
    damageDescription = damageDescription,
    damageLevelIndicator = damageLevelIndicator,
    dateUploaded = dateUploaded,
    imageUris = imageUris

)

fun DamageReport.toPresentation() = DamageReportUI(
    id = id,
    damageDescription = damageDescription,
    damageLevelIndicator = damageLevelIndicator,
    dateUploaded = dateUploaded,
    imageUris = imageUris
)