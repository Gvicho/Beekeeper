package com.example.beekeeper.presenter.event.analytic_details

sealed class AnalyticDetailsEvent {
    data object ResetErrorMessage: AnalyticDetailsEvent()
    data class LoadAnalyticDetails(val id:Int): AnalyticDetailsEvent()
}