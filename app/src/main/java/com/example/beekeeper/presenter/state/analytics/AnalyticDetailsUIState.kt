package com.example.beekeeper.presenter.state.analytics

import com.example.beekeeper.presenter.model.beehive_analytics.analytics_wrapper.AnalyticsWrapper

data class AnalyticDetailsUIState(
    val isLoading :Boolean = false,
    val errorMessage :String? = null,
    val id : Int? = null,
    val saveTime:Long? = null,
    val chartList: List<AnalyticsWrapper>? = null
) {
}