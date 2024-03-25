package com.example.beekeeper.presenter.screen.damaged_beehives.damage_report_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.damage_report.GetReportByIdUseCase
import com.example.beekeeper.presenter.event.damage_beehives.DamageReportDetailsPageEvent
import com.example.beekeeper.presenter.mappers.damage_report.toPresentation
import com.example.beekeeper.presenter.state.damage_report.details.DamageReportDetailsPageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DamageReportDetailsViewModel @Inject constructor(private val getReportByIdUseCase: GetReportByIdUseCase) :
    ViewModel() {

    private val _reportFlow = MutableStateFlow(DamageReportDetailsPageState())
    val reportFlow: StateFlow<DamageReportDetailsPageState> = _reportFlow.asStateFlow()


    fun onEvent(event:DamageReportDetailsPageEvent){
        when(event){
            is DamageReportDetailsPageEvent.GetReportDetailsEvent -> getReportById(event.id)
            DamageReportDetailsPageEvent.ResetErrorMessage -> resetErrorMessageOfReportStateToNull()
        }
    }


    private fun getReportById(id: Int) {
        viewModelScope.launch {
            getReportByIdUseCase.invoke(id).collect {result->
                when (result) {
                    is Resource.Loading -> _reportFlow.update {
                        it.copy( isLoading = true)
                    }
                    is Resource.Success -> {
                        _reportFlow.update {
                            it.copy(reportDetails = result.responseData.toPresentation() ,isLoading = false)
                        }
                    }

                    is Resource.Failed -> {
                        _reportFlow.update {
                            it.copy(errorMessage = result.message, isLoading = false)
                        }
                    }

                }
            }
        }
    }

    private fun resetErrorMessageOfReportStateToNull(){
        _reportFlow.update {
            it.copy(errorMessage = null)
        }
    }

}