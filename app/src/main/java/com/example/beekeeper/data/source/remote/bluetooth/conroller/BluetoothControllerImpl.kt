package com.example.beekeeper.data.source.remote.bluetooth.conroller

import android.bluetooth.BluetoothSocket
import android.content.Context
import com.example.beekeeper.domain.common.SocketConnectionResult
import com.example.beekeeper.domain.controller.bluetooth.BluetoothController
import com.example.beekeeper.domain.model.bluetooth_device.BluetoothDeviceDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

class BluetoothControllerImpl(
    private val context: Context
) : BluetoothController{

    override val isConnected: StateFlow<Boolean>
        get() = TODO("Not yet implemented")
    override val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = TODO("Not yet implemented")
    override val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = TODO("Not yet implemented")
    override val errors: SharedFlow<String>
        get() = TODO("Not yet implemented")



    override fun startDiscovery() {
        TODO("Not yet implemented")
    }

    override fun stopDiscovery() {
        TODO("Not yet implemented")
    }

    override fun startBluetoothServer(): Flow<SocketConnectionResult> {
        TODO("Not yet implemented")
    }

    override fun connectToDevice(device: BluetoothDeviceDomain): Flow<SocketConnectionResult> {
        TODO("Not yet implemented")
    }

    override fun closeConnection() {
        TODO("Not yet implemented")
    }

    override fun release() {
        TODO("Not yet implemented")
    }

    override suspend fun handleInputStream(socket: BluetoothSocket) {
        TODO("Not yet implemented")
    }
}