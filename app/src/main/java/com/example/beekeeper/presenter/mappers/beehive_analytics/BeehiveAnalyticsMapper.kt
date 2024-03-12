package com.example.beekeeper.presenter.mappers.beehive_analytics

import com.example.beekeeper.domain.model.analytics.BeehiveAnalytics
import com.example.beekeeper.presenter.model.beehive_analytics.BeehiveAnalyticsUI

fun BeehiveAnalytics.toUI():BeehiveAnalyticsUI{
    return BeehiveAnalyticsUI(
        id = id,
        weightData = weightData.map {
            it.toFloat()
        },
        temperatureData = temperatureData.map {
            it.toFloat()
        },
        saveDateTime = saveDateTime
    )
}

fun BeehiveAnalyticsUI.toDomain(): BeehiveAnalytics {
    return BeehiveAnalytics(
        id = id,
        weightData = weightData.map {
            it.toDouble()
        },
        temperatureData = temperatureData.map {
            it.toDouble()
        },
        saveDateTime = saveDateTime
    )
}