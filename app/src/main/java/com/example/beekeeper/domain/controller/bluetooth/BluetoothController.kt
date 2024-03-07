package com.example.beekeeper.domain.controller.bluetooth

import android.bluetooth.BluetoothSocket
import com.example.beekeeper.domain.common.SocketConnectionResult
import com.example.beekeeper.domain.model.bluetooth_device.BluetoothDeviceDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {

    val isConnected: StateFlow<Boolean>

    val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
    val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>

    val errors: SharedFlow<String>

    fun startDiscovery()
    fun stopDiscovery()

    fun startBluetoothServer(): Flow<SocketConnectionResult>  // flow will be in a background and inform app when there is a change in connection

    fun connectToDevice(device: BluetoothDeviceDomain): Flow<SocketConnectionResult>// flow will be in a background and inform app when there is a change in connection

    fun closeConnection()

    fun release()

    suspend fun handleInputStream(socket: BluetoothSocket)

}