package com.example.beekeeper.presenter.model.beehive_analytics.analytics_wrapper

data class AnalyticsWrapper(
    val id:Int,
    val analyticList:List<Float>,
    val label:String,
    val analyticType:AnalyticType
)


enum class AnalyticType{
    BAR_CHART,
    LINE_CHART
}