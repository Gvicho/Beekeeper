package com.example.beekeeper.presenter.state.damage_report.details

import com.example.beekeeper.presenter.model.damaged_beehives.DamageReportUI

data class DamageReportDetailsPageState(
    val isLoading:Boolean = false,
    val errorMessage:String? = null,
    val reportDetails: DamageReportUI? = null
) {
}