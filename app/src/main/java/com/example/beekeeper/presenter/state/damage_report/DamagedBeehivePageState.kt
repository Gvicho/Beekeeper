package com.example.beekeeper.presenter.state.damage_report

import com.example.beekeeper.presenter.model.damaged_beehives.DamageReportUI

data class DamagedBeehivePageState(
    val isLoading:Boolean = false,
    val currentReportsList: List<DamageReportUI>? = null,
    val errorMessage :String? = null
)