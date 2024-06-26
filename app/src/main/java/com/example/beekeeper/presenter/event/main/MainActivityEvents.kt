package com.example.beekeeper.presenter.event.main

sealed class MainActivityEvents {
    data object LogOutEvent:MainActivityEvents()

    data object ReadLanguageConfiguration:MainActivityEvents()
    data object ReadDarkMode:MainActivityEvents()

    data object ResetNavigationToNull:MainActivityEvents()

    data class IntentReceivedWithReportId(val reportId:Int):MainActivityEvents()
}