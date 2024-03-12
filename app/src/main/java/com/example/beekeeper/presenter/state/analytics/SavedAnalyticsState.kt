package com.example.beekeeper.presenter.state.analytics

import com.example.beekeeper.presenter.model.saved_analytics.SavedAnalyticsPartialUI

data class SavedAnalyticsState(
    val isLoading :Boolean = false,
    val savedBeehiveAnalyticsList : List<SavedAnalyticsPartialUI>? = null,
    val errorMessage :String? = null
) {
}