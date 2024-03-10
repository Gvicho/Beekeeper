package com.example.beekeeper.presenter.screen.damaged_beehives

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.BeehiveAnalytics
import com.example.beekeeper.domain.usecase.beehive_analytics.InsertAnalyticsUseCase
import com.example.beekeeper.domain.usecase.damage_report.GetAllReportsUseCase
import com.example.beekeeper.presenter.mappers.toPresentation
import com.example.beekeeper.presenter.model.damaged_beehives.DamageReportUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DamagedBeehivesViewModel @Inject constructor(
    private val getAllReportsUseCase: GetAllReportsUseCase,
    private val insertAnalyticsUseCase: InsertAnalyticsUseCase
) :
    ViewModel() {


    private val _reportsFlow = MutableSharedFlow<Resource<List<DamageReportUI>>>()
    val reportsFlow: SharedFlow<Resource<List<DamageReportUI>>> = _reportsFlow.asSharedFlow()


    fun getReports() {
        viewModelScope.launch {
            getAllReportsUseCase.invoke().collect() {
                when (it) {
                    is Resource.Loading -> _reportsFlow.emit(Resource.Loading())
                    is Resource.Success -> {
                        _reportsFlow.emit(Resource.Success(it.responseData.map { report ->
                            report.toPresentation()
                        }))
                    }

                    is Resource.Failed -> _reportsFlow.emit(Resource.Failed(it.message))
                }
            }
        }
    }

    fun test() {
        viewModelScope.launch {
            insertAnalyticsUseCase.invoke(
                BeehiveAnalytics(
                    id = 9881,
                    weightData = listOf(),
                    temperatureData = listOf()
                )
            )
        }
    }
}