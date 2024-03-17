package com.example.beekeeper.presenter.screen.home.farm_details_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.farms.GetFarmDetailsByIdUseCase
import com.example.beekeeper.presenter.event.home.FarmDetailsEvent
import com.example.beekeeper.presenter.mappers.home.details.toUi
import com.example.beekeeper.presenter.state.home.FarmDetailsStateUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class FarmDetailsViewModel@Inject constructor(
    private val getFarmDetailsByIdUseCase: GetFarmDetailsByIdUseCase
):ViewModel() {

    private val _farmDetailsState =  MutableStateFlow(FarmDetailsStateUi())
    val farmDetailsState : StateFlow<FarmDetailsStateUi> = _farmDetailsState

    fun onEvent(event: FarmDetailsEvent){
        when(event){
            is FarmDetailsEvent.LoadFarmDetailsById -> loadFarmById(event.farmId)
            FarmDetailsEvent.ResetErrorMessage -> resetErrorMessageToNull()
        }
    }

    private fun loadFarmById(farmId:Int){
        viewModelScope.launch {
            getFarmDetailsByIdUseCase(farmId).collect{ result->
                when (result) {
                    is Resource.Loading -> {
                        _farmDetailsState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        val farmDetails = withContext(Dispatchers.Default){ result.responseData.toUi() }
                        _farmDetailsState.update {
                            it.copy(farmDetails = farmDetails, isLoading = false)
                        }
                    }
                    is Resource.Failed -> {
                        _farmDetailsState.update {
                            it.copy(errorMessage = result.message, isLoading = false)
                        }
                    }
                }
            }
        }
    }

    private fun resetErrorMessageToNull(){
        _farmDetailsState.update {
            it.copy(errorMessage = null)
        }
    } // header: name id location, images pager:, owner: , beehive analytics: current beehives beehive chart

}