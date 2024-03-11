package com.example.beekeeper.presenter.event.get_analytics

import com.example.beekeeper.presenter.model.beehive_analytics.BeehiveAnalyticsUI

sealed class AnalyticsPreviewEvent {
    data class SaveAnalytics(val beehiveAnalyticsUI: BeehiveAnalyticsUI):AnalyticsPreviewEvent()
    data object ResetErrorMessageToNull : AnalyticsPreviewEvent()
}