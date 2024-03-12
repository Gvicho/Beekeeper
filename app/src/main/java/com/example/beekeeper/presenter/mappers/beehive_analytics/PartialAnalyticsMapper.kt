package com.example.beekeeper.presenter.mappers.beehive_analytics

import com.example.beekeeper.domain.model.analytics.SavedAnalyticsPartial
import com.example.beekeeper.presenter.model.beehive_analytics.saved_analytics.SavedAnalyticsPartialUI
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun SavedAnalyticsPartial.toUI(): SavedAnalyticsPartialUI {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val dateString = dateFormat.format(Date(saveTime))
    return SavedAnalyticsPartialUI(
        beehiveId = beehiveId,
        saveTime = dateString
    )
}