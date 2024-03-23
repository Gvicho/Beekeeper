package com.example.beekeeper.presenter.screen.home.farm_details_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Result
import com.example.beekeeper.domain.usecase.farms.GetFarmDetailsByIdUseCase
import com.example.beekeeper.domain.usecase.weather.GetWeatherUseCase
import com.example.beekeeper.presenter.event.home.FarmDetailsEvent
import com.example.beekeeper.presenter.extension.asUiText
import com.example.beekeeper.presenter.mappers.home.details.toUi
import com.example.beekeeper.presenter.mappers.home.toUI
import com.example.beekeeper.presenter.mappers.weather.toPresentation
import com.example.beekeeper.presenter.model.home.LocationUi
import com.example.beekeeper.presenter.model.home.details.FarmDetailsItemWrapper
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
    private val getFarmDetailsByIdUseCase: GetFarmDetailsByIdUseCase,
    private val getWeatherUseCase: GetWeatherUseCase
):ViewModel() {

    private val _farmDetailsState =  MutableStateFlow(FarmDetailsStateUi())
    val farmDetailsState : StateFlow<FarmDetailsStateUi> = _farmDetailsState

    private lateinit var location :LocationUi


    fun onEvent(event: FarmDetailsEvent){
        when(event){
            is FarmDetailsEvent.LoadFarmDetailsById -> loadFarmDetailsById(event.farmId)
            FarmDetailsEvent.ResetErrorMessage -> resetErrorMessageToNull()
        }
    }

    private fun loadFarmDetailsById(farmId:Int){
        viewModelScope.launch {
            loadFarmDetails(farmId)
            loadWeather() // if this was written with async then both results would be dependent on each other
        }
    }

    private suspend fun loadFarmDetails(farmId:Int){
        getFarmDetailsByIdUseCase(farmId).collect{ result->
            when (result) {
                is Result.Loading -> {
                    _farmDetailsState.update {
                        it.copy(isLoading = true)
                    }
                }
                is Result.Success -> {
                    location = result.responseData.location.toUI()
                    val farmDetails = withContext(Dispatchers.Default){ result.responseData.toUi() }
                    _farmDetailsState.update {
                        it.copy(farmDetails = farmDetails, isLoading = false)
                    }
                }
                is Result.Failed -> {
                    val errorText =  result.error.asUiText()

                    _farmDetailsState.update {
                        it.copy(errorMessage = errorText, isLoading = false)
                    }
                }
            }
        }
    }

    private suspend fun loadWeather(){
        if(!::location.isInitialized)return // there was an error while loading farm info
        val lat = location.latitude
        val lon = location.longitude
        getWeatherUseCase(lat,lon).collect{ result->
            when (result) {
                is Result.Loading -> {
                    // no weather loader needed
                }
                is Result.Success -> {
                    val weatherInfoWrapped = withContext(Dispatchers.Default){ result.responseData.toPresentation() }

                    _farmDetailsState.update {
                        it.copy(
                            farmDetails = it.farmDetails?.map {wrapperItem->
                                if(wrapperItem.itemType == FarmDetailsItemWrapper.ItemType.WEATHER_INFO)weatherInfoWrapped
                                else wrapperItem
                            },
                            isLoading = false
                        )
                    }
                }
                is Result.Failed -> {
                    val errorText =  result.error.asUiText()
                    _farmDetailsState.update {
                        it.copy(errorMessage = errorText, isLoading = false)
                    }
                }
            }
        }
    }

    private fun resetErrorMessageToNull(){
        _farmDetailsState.update {
            it.copy(errorMessage = null)
        }
    }

}