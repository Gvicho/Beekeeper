package com.example.beekeeper.domain.usecase.bluetooth

import com.example.beekeeper.domain.controller.bluetooth.BluetoothController
import com.example.beekeeper.domain.model.bluetooth_device.BluetoothDeviceDomainModel
import javax.inject.Inject

class ConnectToBluetoothDeviceUseCase@Inject constructor(
    private val bluetoothController: BluetoothController
) {

    operator fun invoke(device: BluetoothDeviceDomainModel) = bluetoothController.connectToDevice(device)

}