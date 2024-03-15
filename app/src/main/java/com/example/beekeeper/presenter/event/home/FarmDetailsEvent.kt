package com.example.beekeeper.presenter.event.home

sealed class FarmDetailsEvent {
    data object ResetErrorMessage: FarmDetailsEvent()
    data class LoadFarmDetailsById(val farmId:Int):FarmDetailsEvent()
}