package com.example.beekeeper.domain.usecase.bluetooth

import com.example.beekeeper.domain.controller.bluetooth.BluetoothController
import javax.inject.Inject

class HandleBluetoothInputUseCase@Inject constructor(
    private val bluetoothController: BluetoothController
) {

    suspend operator fun invoke() = bluetoothController.handleInputStream()

}