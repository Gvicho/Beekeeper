package com.example.beekeeper.presenter.event.home

sealed class HomePageEvent {
    data class LoadFarmsList(val searchWord:String): HomePageEvent()
    data object ResetErrorMessage: HomePageEvent()
}