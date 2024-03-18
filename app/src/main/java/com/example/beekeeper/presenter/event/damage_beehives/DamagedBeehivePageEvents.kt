package com.example.beekeeper.presenter.event.damage_beehives

sealed class DamagedBeehivePageEvents {
    data object GetReports:DamagedBeehivePageEvents()
    data object ResetErrorMessageToNull:DamagedBeehivePageEvents()
}