package com.example.beekeeper.presenter.event.get_analytics

import com.example.beekeeper.presenter.model.bluetooth_device.BluetoothDeviceUIModel

sealed class ScanEvent {
    data object StartScan:ScanEvent()
    data object StopScan:ScanEvent() // no need to handle , can call as many times as we want. no issues
    data object ResetErrorMessageToNull:ScanEvent()
    data class ConnectToDevice(val device: BluetoothDeviceUIModel):ScanEvent()
}