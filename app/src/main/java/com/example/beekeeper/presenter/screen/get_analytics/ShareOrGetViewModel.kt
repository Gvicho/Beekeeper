package com.example.beekeeper.presenter.screen.get_analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.bluetooth.HandleBluetoothInputUseCase
import com.example.beekeeper.domain.usecase.bluetooth.ReleaseBluetoothControllerUseCase
import com.example.beekeeper.presenter.event.get_analytics.GetAnalyticsEvent
import com.example.beekeeper.presenter.mappers.beehive_analytics.toUI
import com.example.beekeeper.presenter.model.beehive_analytics.BeehiveAnalyticsUI
import com.example.beekeeper.presenter.state.get_analytics.ReceivedBeehiveAnalyticsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareOrGetViewModel @Inject constructor(
    private val releaseBluetoothControllerUseCase: ReleaseBluetoothControllerUseCase,
    private val handleBluetoothInputUseCase: HandleBluetoothInputUseCase
):ViewModel() {

    private val _receivedBeehiveAnalyticsState =  MutableStateFlow(ReceivedBeehiveAnalyticsState())
    val receivedBeehiveAnalyticsState : StateFlow<ReceivedBeehiveAnalyticsState> = _receivedBeehiveAnalyticsState


    private val _pageNavigationEvent = MutableSharedFlow<GetAnalyticsPageNavigationEvents>()
    val pageNavigationEvent get() = _pageNavigationEvent.asSharedFlow()


    fun onEvent(event:GetAnalyticsEvent){
        when(event){
            GetAnalyticsEvent.HandleInput -> handleInput()
            GetAnalyticsEvent.ResetErrorMessageToNull -> resetErrorMessageToNull()
            GetAnalyticsEvent.NavigateToSavedAnalytics -> navigationEventToSavedAnalytic()
        }
    }

    private fun handleInput(){
        viewModelScope.launch {
            handleBluetoothInputUseCase().collect{result->
                when(result){
                    is Resource.Failed -> {
                        _receivedBeehiveAnalyticsState.update {
                            it.copy(isLoading = false, errorMessage = result.message)
                        }
                    }
                    is Resource.Loading -> {
                        _receivedBeehiveAnalyticsState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        _receivedBeehiveAnalyticsState.update {
                            it.copy(isLoading = false,receivedBeehiveAnalytics = Unit)
                        }
                        val receivedAnalytics = result.responseData.toUI()
                        _pageNavigationEvent.emit(
                            GetAnalyticsPageNavigationEvents.NavigateToAnalyticsPreviewPage(receivedAnalytics)
                        )
                    }
                }
            }
        }
    }

    private fun resetErrorMessageToNull(){
        _receivedBeehiveAnalyticsState.update {
            it.copy(errorMessage = null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        releaseBluetoothControllerUseCase()
    }

    private fun navigationEventToSavedAnalytic(){
        viewModelScope.launch {
            _pageNavigationEvent.emit(GetAnalyticsPageNavigationEvents.NavigateToSavedAnalyticsPage)
        }
    }

    sealed interface GetAnalyticsPageNavigationEvents{
        data class NavigateToAnalyticsPreviewPage(val beehiveAnalytics:BeehiveAnalyticsUI): GetAnalyticsPageNavigationEvents
        data object NavigateToSavedAnalyticsPage:GetAnalyticsPageNavigationEvents
    }

}