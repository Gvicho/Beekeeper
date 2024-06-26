package com.example.beekeeper.presenter.screen.get_analytics.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.SocketConnectionResult
import com.example.beekeeper.domain.controller.bluetooth.BluetoothController
import com.example.beekeeper.domain.usecase.bluetooth.CloseBluetoothConnectionUseCase
import com.example.beekeeper.domain.usecase.bluetooth.ConnectToBluetoothDeviceUseCase
import com.example.beekeeper.domain.usecase.bluetooth.StartBluetoothScanUseCase
import com.example.beekeeper.domain.usecase.bluetooth.StopBluetoothScanUseCase
import com.example.beekeeper.presenter.event.get_analytics.ScanEvent
import com.example.beekeeper.presenter.mappers.bluetooth.toDomain
import com.example.beekeeper.presenter.mappers.bluetooth.toUI
import com.example.beekeeper.presenter.model.bluetooth_device.BluetoothDeviceUIModel
import com.example.beekeeper.presenter.state.bluetooth_beehive.ScanPageUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val startBluetoothScanUseCase: StartBluetoothScanUseCase,
    private val stopBluetoothScanUseCase: StopBluetoothScanUseCase,
    private val connectToBluetoothDeviceUseCase: ConnectToBluetoothDeviceUseCase,
    private val closeBluetoothConnectionUseCase: CloseBluetoothConnectionUseCase,
    bluetoothController: BluetoothController
) : ViewModel() {

    private val _state = MutableStateFlow(ScanPageUIState())
    val state = combine(
        bluetoothController.pairedDevices,
        bluetoothController.scannedDevices,
        _state
    ){pairedDevices,scannedDevices,state ->  // if any of them changes
        state.copy(
            scannedDevices = withContext(Dispatchers.Default){scannedDevices.map { it.toUI() }} ,
            pairedDevices = withContext(Dispatchers.Default){pairedDevices.map { it.toUI() }}
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),_state.value)  // having use case instead of this was hard. then We had to use Scope in domain in order to have it inside stateIn()

    private var deviceConnectionJob : Job? = null
    init {
        bluetoothController.isConnected.onEach {isConnected ->
            _state.update { it.copy(isConnected = isConnected) }
        }.launchIn(viewModelScope)

        bluetoothController.errors.onEach {errorMessage->
            _state.update { it.copy(errorMessage = errorMessage ) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event:ScanEvent){
        when(event){
            is ScanEvent.ConnectToDevice -> connectToDevice(event.device)
            ScanEvent.ResetErrorMessageToNull -> onResetErrorMessage()
            ScanEvent.StartScan -> startScan()
            ScanEvent.StopScan -> stopScan()
        }
    }

    private fun startScan(){
        startBluetoothScanUseCase()
    }

    private fun stopScan(){
        stopBluetoothScanUseCase()
    }


    private fun connectToDevice(device:BluetoothDeviceUIModel){
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = connectToBluetoothDeviceUseCase(device.toDomain()).listen() //  this can be changed with handler
        // we save it here so that we can cancel it when we want it
    }


    private fun Flow<SocketConnectionResult>.listen(): Job {  // we can keep track of this job, and maybe cancel if we want it
        return onEach{result->
            when(result){
                SocketConnectionResult.ConnectionEstablished -> {
                    _state.update { it.copy(
                        isConnected = true,
                        isConnecting = false,
                        errorMessage = null
                    ) }
                }
                is SocketConnectionResult.Error -> {
                    _state.update { it.copy(
                        isConnected = false,
                        isConnecting = false,
                        errorMessage = result.errorMessage
                    ) }
                }
            }
        }.catch {throwable ->
            // everything we catch in flow will be trowed here
            closeBluetoothConnectionUseCase()
            _state.update { it.copy(
                isConnected = false,
                isConnecting = false,
                errorMessage = throwable.message
            ) }
        }.launchIn(viewModelScope)
    }

    private fun onResetErrorMessage(){
        _state.update { it.copy(errorMessage = null) }
    }

    override fun onCleared() {
        super.onCleared()
        stopScan()
    }

}