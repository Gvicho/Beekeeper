package com.example.beekeeper.domain.usecase.bluetooth

import com.example.beekeeper.domain.controller.bluetooth.BluetoothController
import javax.inject.Inject

class ReleaseBluetoothControllerUseCase @Inject constructor(
    private val bluetoothController: BluetoothController
) {

    operator fun invoke(){
        bluetoothController.release()
    }

}