package com.example.beekeeper.presenter.state.damage_report

data class DamageDescriptionState(
    val isLoading:Boolean = false,
    val description:String? = null,
    val errorMessage:String? = null
) {
}