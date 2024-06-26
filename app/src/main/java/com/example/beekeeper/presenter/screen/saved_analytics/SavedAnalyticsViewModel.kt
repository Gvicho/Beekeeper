package com.example.beekeeper.presenter.screen.saved_analytics

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.analytics.BeehiveAnalytics
import com.example.beekeeper.domain.usecase.beehive_analytics.DeleteAnalyticsByIdUseCase
import com.example.beekeeper.domain.usecase.beehive_analytics.GetAllAnalyticsUseCase
import com.example.beekeeper.domain.usecase.beehive_analytics.GetAnalyticsListByIdUseCase
import com.example.beekeeper.domain.usecase.beehive_analytics.upload.UploadAnalyticsListUseCase
import com.example.beekeeper.domain.utils.Order
import com.example.beekeeper.presenter.event.saved_analytics.SavedAnalyticsEvent
import com.example.beekeeper.presenter.mappers.beehive_analytics.toUI
import com.example.beekeeper.presenter.state.analytics.SavedAnalyticsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SavedAnalyticsViewModel@Inject constructor(
    private val getAllAnalyticsUseCase: GetAllAnalyticsUseCase,
    private val deleteAnalyticsByIdUseCase: DeleteAnalyticsByIdUseCase,
    private val uploadAnalyticsListUseCase: UploadAnalyticsListUseCase,
    private val getAnalyticsListByIdUseCase: GetAnalyticsListByIdUseCase
): ViewModel() {

    private val _beehiveAnalyticsState =  MutableStateFlow(SavedAnalyticsState())
    val beehiveAnalyticsState : StateFlow<SavedAnalyticsState> = _beehiveAnalyticsState.asStateFlow()

    private val _savedAnalyticsPageNavigationEvent = MutableSharedFlow<SavedAnalyticsNavigationEvents>()
    val savedAnalyticsPageNavigationEvent get() = _savedAnalyticsPageNavigationEvent.asSharedFlow()

    fun onEvent(event:SavedAnalyticsEvent){
        when(event){
            SavedAnalyticsEvent.ResetErrorMessageToNull -> updateErrorMessageToNull()
            SavedAnalyticsEvent.DeleteAnalytics -> deleteAnalytics()
            is SavedAnalyticsEvent.OnItemClick -> onClick(event.id)
            is SavedAnalyticsEvent.OnLongItemClick -> onLongClick(event.id)
            SavedAnalyticsEvent.UploadAnalyticsOnDataBase -> uploadAnalytics()
            SavedAnalyticsEvent.ResetUploadSuccessMessageToNull -> updateErrorUploadSuccessMessageToNull()
            is SavedAnalyticsEvent.LoadAnalyticsOrder -> loadAnalytics(event.order)
        }
    }

    private fun loadAnalytics(order:Order){
        viewModelScope.launch {
            getAllAnalyticsUseCase(order).collect {result->
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

                        _beehiveAnalyticsState.update {state->
                            state.copy(savedBeehiveAnalyticsList = list.map {
                                if(it.isSelected)it.copy(isSelected = false)
                                else it },
                                isLoading = false, order = order,
                                selectedItemsList = emptyList()
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

    private fun updateErrorMessageToNull(){
        _beehiveAnalyticsState.update {
            it.copy(errorMessage = null)
        }
    }

    private fun updateErrorUploadSuccessMessageToNull(){
        _beehiveAnalyticsState.update {
            it.copy(uploadSuccessful = null)
        }
    }

    private fun deleteAnalytics(){
        val list = _beehiveAnalyticsState.value.selectedItemsList
        list.forEach{
            deleteAnalyticsById(it)
        }
        viewModelScope.launch{
            _beehiveAnalyticsState.update {
                it.copy(selectedItemsList = emptyList())
            }
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
                                savedBeehiveAnalyticsList = analyticsState.savedBeehiveAnalyticsList?.filter { it.beehiveId!=id }
                            )
                        }

                    }
                    is Resource.Failed -> {
                        _beehiveAnalyticsState.update {
                            it.copy(errorMessage = "id: $id :${result.message}", isLoading = false)
                        }
                    }
                }

            }
        }
    }

    private fun onClick(id:Int){
        val state = _beehiveAnalyticsState.value
        viewModelScope.launch {
            if(state.selectedItemsList.isEmpty()){
                navigationEventToSavedAnalytic(id)
            }else{
                _beehiveAnalyticsState.update {savedState->
                    if(savedState.selectedItemsList.contains(id)){
                        Log.d("tag1234","onClick erase id: $id")
                        savedState.copy(
                            selectedItemsList = savedState.selectedItemsList.filter { it!=id },
                            savedBeehiveAnalyticsList = savedState.savedBeehiveAnalyticsList?.map { if(it.beehiveId == id)it.copy(isSelected = !it.isSelected) else it }
                        )
                    }else{
                        Log.d("tag1234","onClick insert id: $id")
                        savedState.copy(
                            selectedItemsList = savedState.selectedItemsList+id,
                            savedBeehiveAnalyticsList = savedState.savedBeehiveAnalyticsList?.map { if(it.beehiveId == id)it.copy(isSelected = !it.isSelected) else it }
                        )
                    }

                }
            }
        }

    }

    private fun onLongClick(id:Int){
        _beehiveAnalyticsState.update {savedState->
            if(savedState.selectedItemsList.contains(id)){
                Log.d("tag1234","onLong erase id: $id")
                savedState.copy(
                    selectedItemsList = savedState.selectedItemsList.filter { it!=id },
                    savedBeehiveAnalyticsList = savedState.savedBeehiveAnalyticsList?.map { if(it.beehiveId == id)it.copy(isSelected = !it.isSelected) else it }
                )
            }else{
                Log.d("tag1234","onLong insert id: $id")
                savedState.copy(
                    selectedItemsList = savedState.selectedItemsList+id,
                    savedBeehiveAnalyticsList = savedState.savedBeehiveAnalyticsList?.map { if(it.beehiveId == id)it.copy(isSelected = !it.isSelected) else it }
                )
            }
        }
    }

    private fun navigationEventToSavedAnalytic(beehiveId: Int){
        viewModelScope.launch {
            _savedAnalyticsPageNavigationEvent.emit(SavedAnalyticsNavigationEvents.NavigateToAnalyticPreviewPage(beehiveId))
        }
    }

    private fun uploadAnalytics(){

        val ids = _beehiveAnalyticsState.value.selectedItemsList

        val analyticsDeferred = viewModelScope.async(Dispatchers.IO) {
            val resultList = mutableListOf<BeehiveAnalytics>()
            getAnalyticsListByIdUseCase(ids).collect { resource ->
                if (resource is Resource.Success) {
                    resultList.addAll(resource.responseData)
                }
            }
            resultList
        }

        viewModelScope.launch {
            uploadAnalyticsListUseCase(analyticsDeferred.await()).collect{result->
                when(result){
                    is Resource.Failed -> {
                        _beehiveAnalyticsState.update {
                            it.copy(errorMessage = result.message, isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _beehiveAnalyticsState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        _beehiveAnalyticsState.update {state->
                            state.copy(uploadSuccessful = Unit,
                                isLoading = false,
                                selectedItemsList = emptyList(),
                                savedBeehiveAnalyticsList = state.savedBeehiveAnalyticsList?.map {
                                    if(it.isSelected)it.copy(isSelected = false)
                                    else it
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    sealed interface SavedAnalyticsNavigationEvents{
        data class NavigateToAnalyticPreviewPage(val beehiveId:Int): SavedAnalyticsNavigationEvents
    }
}