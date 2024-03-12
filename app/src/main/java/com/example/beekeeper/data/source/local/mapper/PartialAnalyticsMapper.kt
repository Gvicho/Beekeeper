package com.example.beekeeper.data.source.local.mapper

import com.example.beekeeper.data.source.local.model.SavedAnalyticsPartialData
import com.example.beekeeper.domain.model.analytics.SavedAnalyticsPartial

fun SavedAnalyticsPartialData.toDomain(): SavedAnalyticsPartial {
    return SavedAnalyticsPartial(
        beehiveId = id,
        saveTime = saveDateTime
    )
}
