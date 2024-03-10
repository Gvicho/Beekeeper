package com.example.beekeeper.presenter.screen.share_or_get_analytics.view_received_analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.beehive_analytics.InsertAnalyticsUseCase
import com.example.beekeeper.presenter.event.get_analytics.AnalyticsPreviewEvent
import com.example.beekeeper.presenter.mappers.beehive_analytics.toDomain
import com.example.beekeeper.presenter.model.beehive_analytics.BeehiveAnalyticsUI
import com.example.beekeeper.presenter.state.get_analytics.SaveAnalyticsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ReceivedAnalyticsViewModel@Inject constructor(
    private val insertAnalyticsUseCase: InsertAnalyticsUseCase
): ViewModel() {

    private val _saveAnalyticsState =  MutableStateFlow(SaveAnalyticsState())
    val saveAnalyticsState : StateFlow<SaveAnalyticsState> = _saveAnalyticsState

    private val _pageNavigationEvent = MutableSharedFlow<GetAnalyticsPageNavigationEvents>()
    val pageNavigationEvent get() = _pageNavigationEvent.asSharedFlow()

    fun onEvent(event: AnalyticsPreviewEvent){
        when(event){
            is AnalyticsPreviewEvent.SaveAnalytics -> saveAnalytics(event.beehiveAnalyticsUI)
            AnalyticsPreviewEvent.ResetErrorMessageToNull -> resetErrorMessageToNull()
        }
    }

    private fun saveAnalytics(beehiveAnalyticsUI: BeehiveAnalyticsUI){
        viewModelScope.launch{
            val beehiveAnalytics = withContext(Dispatchers.Default){beehiveAnalyticsUI.toDomain()}
            insertAnalyticsUseCase(beehiveAnalytics).collect{
                handleResult(it)
            }
        }
    }

    private suspend fun handleResult(result:Resource<Long>){
        when(result){
            is Resource.Failed -> {
                _saveAnalyticsState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }
            }
            is Resource.Loading -> {
                _saveAnalyticsState.update {
                    it.copy(isLoading = true)
                }
            }
            is Resource.Success -> {
                _saveAnalyticsState.update {
                    if(result.responseData == -1L){
                        it.copy(isLoading = false, errorMessage = "Unknown DataBase Error message")
                    }else{
                        it.copy(isLoading = false, saveStatus = Unit)
                    }
                }
                _pageNavigationEvent.emit(
                    GetAnalyticsPageNavigationEvents.NavigateBack
                )
            }
        }
    }

    private fun resetErrorMessageToNull(){
        _saveAnalyticsState.update {
            it.copy(errorMessage = null)
        }
    }

    sealed interface GetAnalyticsPageNavigationEvents{
        data object NavigateBack: GetAnalyticsPageNavigationEvents
    }

}