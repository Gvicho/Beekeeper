package com.example.beekeeper.data.source.remote.internet.mappers.analytics

import com.example.beekeeper.data.source.remote.internet.model.analytics.BeehiveAnalyticsDto
import com.example.beekeeper.domain.model.analytics.BeehiveAnalytics

fun BeehiveAnalytics.toDto(): BeehiveAnalyticsDto {
    return BeehiveAnalyticsDto(
        id = id,
        weightData = weightData,
        temperatureData = temperatureData,
        saveDateTime = saveDateTime
    )
}