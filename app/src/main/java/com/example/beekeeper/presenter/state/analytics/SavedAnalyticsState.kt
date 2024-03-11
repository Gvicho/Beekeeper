package com.example.beekeeper.presenter.state.analytics

import com.example.beekeeper.presenter.model.beehive_analytics.BeehiveAnalyticsUI

data class SavedAnalyticsState(
    val isLoading :Boolean = false,
    val savedBeehiveAnalyticsList : List<BeehiveAnalyticsUI>? = null,
    val errorMessage :String? = null
) {
}