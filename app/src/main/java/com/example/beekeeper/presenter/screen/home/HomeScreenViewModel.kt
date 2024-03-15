package com.example.beekeeper.presenter.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.farms.GetFarmsListUseCase
import com.example.beekeeper.presenter.event.home.HomePageEvent
import com.example.beekeeper.presenter.mappers.home.toUI
import com.example.beekeeper.presenter.state.home.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getFarmsListUseCase: GetFarmsListUseCase
): ViewModel() {

    private val _homeUIState =  MutableStateFlow(HomeScreenState())
    val homeUIState : StateFlow<HomeScreenState> = _homeUIState

//    private val _loginPageNavigationEvent = MutableSharedFlow<>()
//    val loginPageNavigationEvent get() = _loginPageNavigationEvent

    init {
        loadFarmsList()
    }

    fun onEvent(event: HomePageEvent){
        when(event){
            HomePageEvent.LoadFarmsList -> loadFarmsList()  // if there is refresh
            is HomePageEvent.MoveUserToFarmDetailsEvent -> TODO()
            HomePageEvent.ResetErrorMessage -> updateErrorMessage(null)
        }
    }

    private fun updateErrorMessage(message:String?){
        _homeUIState.update {
            it.copy(errorMessage = message)
        }
    }

    private fun loadFarmsList(){
        viewModelScope.launch {
            getFarmsListUseCase().collect {result->
                when (result) {
                    is Resource.Loading -> {
                        _homeUIState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        val list = withContext(Dispatchers.Default){
                            result.responseData.map {
                                it.toUI()
                            }
                        }

                        _homeUIState.update {
                            it.copy(farmList = list, isLoading = false)
                        }
                    }
                    is Resource.Failed -> {
                        _homeUIState.update {
                            it.copy(errorMessage = result.message, isLoading = false)
                        }
                    }
                }

            }
        }
    }


}