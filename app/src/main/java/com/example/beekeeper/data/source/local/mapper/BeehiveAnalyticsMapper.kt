package com.example.beekeeper.data.source.local.mapper

import com.example.beekeeper.data.source.local.model.BeehiveAnalyticsEntity
import com.example.beekeeper.domain.model.analytics.BeehiveAnalytics

fun BeehiveAnalyticsEntity.toDomain(): BeehiveAnalytics {
    return BeehiveAnalytics(
        id = id,
        weightData = weightData.split(",").map { it.toDouble() },
        temperatureData = temperatureData.split(",").map { it.toDouble() },
        saveDateTime = saveDateTime
    )
}

fun BeehiveAnalytics.toEntity(): BeehiveAnalyticsEntity {
    return BeehiveAnalyticsEntity(
        id = id,
        weightData = weightData.joinToString(","),
        temperatureData = temperatureData.joinToString(","),
        saveDateTime = saveDateTime
    )
}