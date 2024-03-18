package com.example.beekeeper.presenter.screen.damaged_beehives

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.damage_report.GetAllReportsUseCase
import com.example.beekeeper.presenter.event.damage_beehives.DamagedBeehivePageEvents
import com.example.beekeeper.presenter.mappers.toPresentation
import com.example.beekeeper.presenter.state.damage_report.DamagedBeehivePageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DamagedBeehivesViewModel @Inject constructor(
    private val getAllReportsUseCase: GetAllReportsUseCase,
) :
    ViewModel() {

    private val _currentReportsFlow = MutableStateFlow(DamagedBeehivePageState())
    val currentReportsFlow: StateFlow<DamagedBeehivePageState> = _currentReportsFlow.asStateFlow()

    fun onEvent(event: DamagedBeehivePageEvents){
        when(event){
            DamagedBeehivePageEvents.GetReports -> getReports()
            DamagedBeehivePageEvents.ResetErrorMessageToNull -> updateErrorMessageToNull()
        }
    }

    private fun getReports() {
        viewModelScope.launch {
            getAllReportsUseCase.invoke().collect{result->
                when (result) {
                    is Resource.Loading -> {
                        _currentReportsFlow.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        val currentReportsList = withContext(Dispatchers.Default){result.responseData.map { it.toPresentation() }}
                        _currentReportsFlow.update {
                            it.copy(isLoading = false, currentReportsList = currentReportsList)
                        }
                    }
                    is Resource.Failed -> {
                        _currentReportsFlow.update {
                            it.copy(isLoading = false, errorMessage = result.message)
                        }
                    }
                }
            }
        }
    }

    private fun updateErrorMessageToNull(){
        _currentReportsFlow.update {
            it.copy(errorMessage = null)
        }
    }


}