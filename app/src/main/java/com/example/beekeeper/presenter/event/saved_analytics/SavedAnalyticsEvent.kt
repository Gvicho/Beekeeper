package com.example.beekeeper.presenter.event.saved_analytics

sealed class SavedAnalyticsEvent {
    data object LoadAnalyticsList: SavedAnalyticsEvent()

    data object DeleteAnalytics: SavedAnalyticsEvent()

    data object UploadAnalyticsOnDataBase: SavedAnalyticsEvent()

    data class OnItemClick(val id:Int):SavedAnalyticsEvent()

    data class OnLongItemClick(val id:Int):SavedAnalyticsEvent()


    data object ResetErrorMessageToNull: SavedAnalyticsEvent()

    data object ResetUploadSuccessMessageToNull: SavedAnalyticsEvent()

}