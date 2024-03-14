package com.example.beekeeper.presenter.screen.saved_analytics.analytic_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.beehive_analytics.GetAnalyticsByIdUseCase
import com.example.beekeeper.presenter.event.analytic_details.AnalyticDetailsEvent
import com.example.beekeeper.presenter.mappers.beehive_analytics.toChartData
import com.example.beekeeper.presenter.mappers.beehive_analytics.toUI
import com.example.beekeeper.presenter.state.analytics.AnalyticDetailsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AnalyticsDetailsViewModel @Inject constructor(
    private val getAnalyticsByIdUseCase: GetAnalyticsByIdUseCase
): ViewModel() {

    private val _beehiveAnalyticDetailsState =  MutableStateFlow(AnalyticDetailsUIState())
    val beehiveAnalyticDetailsState : StateFlow<AnalyticDetailsUIState> = _beehiveAnalyticDetailsState

    fun onEvent(event: AnalyticDetailsEvent){
        when(event){
            is AnalyticDetailsEvent.LoadAnalyticDetails -> loadAnalyticDetails(event.id)
            AnalyticDetailsEvent.ResetErrorMessage -> updateErrorMessageToNull()
        }
    }

    private fun loadAnalyticDetails(id:Int){
        viewModelScope.launch {
            getAnalyticsByIdUseCase(id).collect {result->
                when (result) {
                    is Resource.Loading -> {
                        _beehiveAnalyticDetailsState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        val analytic = result.responseData
                        val chartList = withContext(Dispatchers.Default){
                            analytic.toUI().toChartData()
                        }
                        val beehiveId = analytic.id
                        val saveTime = analytic.saveDateTime

                        _beehiveAnalyticDetailsState.update {
                            it.copy(saveTime = saveTime, id = beehiveId, chartList = chartList, isLoading = false)
                        }
                    }
                    is Resource.Failed -> {
                        _beehiveAnalyticDetailsState.update {
                            it.copy(errorMessage = result.message, isLoading = false)
                        }
                    }
                }

            }
        }
    }

    private fun updateErrorMessageToNull(){
        _beehiveAnalyticDetailsState.update {
            it.copy(errorMessage = null)
        }
    }

}