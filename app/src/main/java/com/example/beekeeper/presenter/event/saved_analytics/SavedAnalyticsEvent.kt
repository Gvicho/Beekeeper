package com.example.beekeeper.presenter.event.saved_analytics

import com.example.beekeeper.presenter.model.beehive_analytics.BeehiveAnalyticsUI

sealed class SavedAnalyticsEvent {
    data object LoadAnalyticsList: SavedAnalyticsEvent()
    data class DeleteAnalyticsOnId(val id:Int): SavedAnalyticsEvent()
    data class UploadAnalyticsOnDataBase(val beehiveAnalyticsUI: BeehiveAnalyticsUI): SavedAnalyticsEvent()
    data object ResetErrorMessageToNull: SavedAnalyticsEvent()
}