package com.example.beekeeper.domain.controller.bluetooth

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.common.SocketConnectionResult
import com.example.beekeeper.domain.model.BeehiveAnalytics
import com.example.beekeeper.domain.model.bluetooth_device.BluetoothDeviceDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {

    val isConnected: StateFlow<Boolean>

    val scannedDevices: StateFlow<List<BluetoothDeviceDomainModel>>
    val pairedDevices: StateFlow<List<BluetoothDeviceDomainModel>>

    val errors: SharedFlow<String>

    fun startDiscovery()
    fun stopDiscovery()

    fun startBluetoothServer(): Flow<SocketConnectionResult>  // flow will be in a background and inform app when there is a change in connection

    fun connectToDevice(device: BluetoothDeviceDomainModel): Flow<SocketConnectionResult>// flow will be in a background and inform app when there is a change in connection

    fun closeConnection()

    fun release()

    suspend fun handleInputStream() : Flow<Resource<BeehiveAnalytics>>

}