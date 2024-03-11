package com.example.beekeeper.presenter.state.bluetooth_beehive

import com.example.beekeeper.presenter.model.bluetooth_device.BluetoothDeviceUIModel

data class ScanPageUIState(
    val scannedDevices:List<BluetoothDeviceUIModel> = emptyList(),
    val pairedDevices: List<BluetoothDeviceUIModel> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting :Boolean = false,
    val errorMessage :String? = null
) {
}