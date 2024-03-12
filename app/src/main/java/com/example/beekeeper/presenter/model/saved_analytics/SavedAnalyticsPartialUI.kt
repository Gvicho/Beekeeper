package com.example.beekeeper.presenter.model.saved_analytics

data class SavedAnalyticsPartialUI(
    val beehiveId:Int,
    val saveTime:String,
    var isSelected:Boolean = false
)