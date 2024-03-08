package com.example.beekeeper.data.source.remote.bluetooth.mapper

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.example.beekeeper.domain.model.bluetooth_device.BluetoothDeviceDomainModel


@SuppressLint("MissingPermission") // this will never be called without first checking the permissions
fun BluetoothDevice.toModel():BluetoothDeviceDomainModel{
    return BluetoothDeviceDomainModel(
        name = name,
        address = address
    )
}