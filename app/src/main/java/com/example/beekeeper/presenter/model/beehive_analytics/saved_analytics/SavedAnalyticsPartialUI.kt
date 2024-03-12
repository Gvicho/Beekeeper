package com.example.beekeeper.presenter.model.beehive_analytics.saved_analytics

data class SavedAnalyticsPartialUI(
    val beehiveId:Int,
    val saveTime:String,
    val isSelected:Boolean = false
)