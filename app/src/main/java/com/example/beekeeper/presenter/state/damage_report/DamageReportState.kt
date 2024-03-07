package com.example.beekeeper.presenter.state.damage_report

data class DamageReportState(
    val isLoading :Boolean = false,
    val uploadSuccess : Unit? = null,
    val errorMessage :String? = null
) {
}