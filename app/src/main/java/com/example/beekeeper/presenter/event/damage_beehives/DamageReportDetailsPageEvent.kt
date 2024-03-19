package com.example.beekeeper.presenter.event.damage_beehives

sealed class DamageReportDetailsPageEvent {
    data object ResetErrorMessage:DamageReportDetailsPageEvent()
    data class GetReportDetailsEvent(val id:Int):DamageReportDetailsPageEvent()
}