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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFarmsListUseCase: GetFarmsListUseCase
): ViewModel() {

    private val _homeUIState =  MutableStateFlow(HomeScreenState())
    val homeUIState : StateFlow<HomeScreenState> = _homeUIState

    private val _searchTextQuery = MutableStateFlow("")
    @FlowPreview  // indicates that it isn't stable to use debounce yet, and it might change in the future, it just warns me
    val searchTextQuery : StateFlow<String> = _searchTextQuery
        .debounce(600) // Debounce time in milliseconds
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ""
        )

    init {
        loadFarmsList("")
        bindTextQueryFlowObserver()
    }

    fun onEvent(event: HomePageEvent){
        when(event){
            is HomePageEvent.LoadFarmsList -> updateSearchTextQuery(event.searchWord)
            HomePageEvent.ResetErrorMessage -> updateErrorMessage()
        }
    }

    private fun updateErrorMessage(){
        _homeUIState.update {
            it.copy(errorMessage = null)
        }
    }

    private fun updateSearchTextQuery(searchWord:String){
        _searchTextQuery.value = searchWord
    }

    @OptIn(FlowPreview::class)
    private fun bindTextQueryFlowObserver(){
        viewModelScope.launch {
            searchTextQuery.collect{
                loadFarmsList(it)
            }
        }
    }

    private fun loadFarmsList(searchWord:String){
        viewModelScope.launch {
            getFarmsListUseCase(searchWord).collect {result->
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