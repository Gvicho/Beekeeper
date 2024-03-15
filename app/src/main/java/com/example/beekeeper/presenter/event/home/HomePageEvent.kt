package com.example.beekeeper.presenter.event.home

sealed class HomePageEvent {
    data object LoadFarmsList: HomePageEvent()
    data class MoveUserToFarmDetailsEvent(val id:Int): HomePageEvent()
    data object ResetErrorMessage: HomePageEvent()
}