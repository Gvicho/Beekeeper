package com.example.beekeeper.presenter.state.analytics

import com.example.beekeeper.presenter.model.beehive_analytics.saved_analytics.SavedAnalyticsPartialUI

data class SavedAnalyticsState(
    val isLoading :Boolean = false,
    val savedBeehiveAnalyticsList : List<SavedAnalyticsPartialUI>? = null,
    val selectedItemsList: List<Int> = emptyList(),
    val errorMessage :String? = null,
    val uploadSuccessful:Unit? = null
) {
}