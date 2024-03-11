package com.example.beekeeper.presenter.screen.saved_analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.beehive_analytics.DeleteAnalyticsByIdUseCase
import com.example.beekeeper.domain.usecase.beehive_analytics.GetAllAnalyticsUseCase
import com.example.beekeeper.presenter.event.saved_analytics.SavedAnalyticsEvent
import com.example.beekeeper.presenter.mappers.beehive_analytics.toUI
import com.example.beekeeper.presenter.state.analytics.SavedAnalyticsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SavedAnalyticsViewModel@Inject constructor(
    private val getAllAnalyticsUseCase: GetAllAnalyticsUseCase,
    private val deleteAnalyticsByIdUseCase: DeleteAnalyticsByIdUseCase
): ViewModel() {

    private val _beehiveAnalyticsState =  MutableStateFlow(SavedAnalyticsState())
    val beehiveAnalyticsState : StateFlow<SavedAnalyticsState> = _beehiveAnalyticsState

    init {
        loadAnalytics()
    }

    fun onEvent(event:SavedAnalyticsEvent){
        when(event){
            is SavedAnalyticsEvent.DeleteAnalyticsOnId -> deleteAnalyticsById(event.id)
            SavedAnalyticsEvent.LoadAnalyticsList -> loadAnalytics()
            SavedAnalyticsEvent.ResetErrorMessageToNull -> updateErrorMessageToNull()
            is SavedAnalyticsEvent.UploadAnalyticsOnDataBase -> TODO()
        }
    }

    private fun loadAnalytics(){
        viewModelScope.launch {
            getAllAnalyticsUseCase().collect {result->
                when (result) {
                    is Resource.Loading -> {
                        _beehiveAnalyticsState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        val list = withContext(Dispatchers.Default){
                            result.responseData.map {
                                it.toUI()
                            }
                        }

                        _beehiveAnalyticsState.update {
                            it.copy(savedBeehiveAnalyticsList = list, isLoading = false)
                        }
                    }
                    is Resource.Failed -> {
                        _beehiveAnalyticsState.update {
                            it.copy(errorMessage = result.message, isLoading = false)
                        }
                    }
                }

            }
        }
    }

    private fun updateErrorMessageToNull(){
        _beehiveAnalyticsState.update {
            it.copy(errorMessage = null)
        }
    }

    private fun deleteAnalyticsById(id:Int){
        viewModelScope.launch {
            deleteAnalyticsByIdUseCase(id).collect {result->
                when (result) {
                    is Resource.Loading -> {
                        _beehiveAnalyticsState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        _beehiveAnalyticsState.update {analyticsState->
                            analyticsState.copy(isLoading = false,
                                savedBeehiveAnalyticsList = analyticsState.savedBeehiveAnalyticsList?.filter { it.id!=id }
                            )
                        }

                    }
                    is Resource.Failed -> {
                        _beehiveAnalyticsState.update {
                            it.copy(errorMessage = result.message, isLoading = false)
                        }
                    }
                }

            }
        }
    }

}