package com.example.beekeeper.presenter.mappers.beehive_analytics

import com.example.beekeeper.presenter.model.beehive_analytics.BeehiveAnalyticsUI
import com.example.beekeeper.presenter.model.beehive_analytics.analytics_wrapper.AnalyticType
import com.example.beekeeper.presenter.model.beehive_analytics.analytics_wrapper.AnalyticsWrapper


fun BeehiveAnalyticsUI.toChartData() : List<AnalyticsWrapper>{
    return listOf(
        AnalyticsWrapper(
            id = 1,
            analyticList = weightData,
            label = "Honey Weight",
            analyticType = AnalyticType.BAR_CHART,
            saveTime = saveDateTime
        ),
        AnalyticsWrapper(
            id = 2,
            analyticList = temperatureData,
            label = "Temperature",
            analyticType = AnalyticType.LINE_CHART,
            saveTime = saveDateTime
        )
    )
}