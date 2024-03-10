package com.example.beekeeper.data.source.local.mapper

import com.example.beekeeper.data.source.local.entity.BeehiveAnalyticsEntity
import com.example.beekeeper.domain.model.BeehiveAnalytics

fun BeehiveAnalyticsEntity.toDomain() =  BeehiveAnalytics(
    id = id,
    weightData = weightData.toList().map { it.code.toDouble() },
    temperatureData = temperatureData.toList().map { it.code.toDouble() },
)

fun BeehiveAnalytics.toData() = BeehiveAnalyticsEntity(
    id = id,
    weightData = weightData.toString(),
    temperatureData = temperatureData.toString()
)