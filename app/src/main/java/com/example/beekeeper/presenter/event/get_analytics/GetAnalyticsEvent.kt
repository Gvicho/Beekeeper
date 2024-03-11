package com.example.beekeeper.presenter.event.get_analytics

sealed class GetAnalyticsEvent {
    data object ResetErrorMessageToNull:GetAnalyticsEvent()
    data object HandleInput:GetAnalyticsEvent()
}