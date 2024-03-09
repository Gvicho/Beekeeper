package com.example.beekeeper.presenter.screen.share_or_get_analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.controller.bluetooth.BluetoothController
import com.example.beekeeper.presenter.state.bluetooth_beehive.ReceivedBeehiveAnalyticsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareOrGetViewModel @Inject constructor(
    private val bluetoothController: BluetoothController
):ViewModel() {

    private val _receivedBeehiveAnalyticsState =  MutableStateFlow(ReceivedBeehiveAnalyticsState())
    val receivedBeehiveAnalyticsState : StateFlow<ReceivedBeehiveAnalyticsState> = _receivedBeehiveAnalyticsState


    fun handleInput(){
        viewModelScope.launch {
            bluetoothController.handleInputStream().collect{result->
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
                            it.copy(isLoading = false,receivedBeehiveAnalytics = result.responseData)
                        }
                    }
                }
            }
        }
    }

    fun resetErrorMessageToNull(){
        _receivedBeehiveAnalyticsState.update {
            it.copy(errorMessage = null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        bluetoothController.release()
    }

}