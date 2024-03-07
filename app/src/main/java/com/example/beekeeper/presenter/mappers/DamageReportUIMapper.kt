package com.example.beekeeper.presenter.mappers

import com.example.beekeeper.domain.model.damaged_beehives.DamageReport
import com.example.beekeeper.presenter.model.damagedBeehives.DamageReportUI


fun DamageReportUI.toDomain() = DamageReport(
    id = id,
    location = location,
    damageDescription = damageDescription,
    damageLevelIndicator = damageLevelIndicator,
    dateUploaded = dateUploaded,
    damageReason = damageReason,
    imageUris = imageUris
)