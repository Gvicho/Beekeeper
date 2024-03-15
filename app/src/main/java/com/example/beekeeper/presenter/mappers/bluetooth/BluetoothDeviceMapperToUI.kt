package com.example.beekeeper.presenter.mappers.bluetooth

import com.example.beekeeper.domain.model.bluetooth_device.BluetoothDeviceDomainModel
import com.example.beekeeper.presenter.model.bluetooth_device.BluetoothDeviceUIModel


fun BluetoothDeviceDomainModel.toUI() : BluetoothDeviceUIModel {
    return BluetoothDeviceUIModel(
        name = name,
        address = address,
        isBeehive = name?.contains("beehive", ignoreCase = true)?:false  // check if in name is beehive
    )
}

fun BluetoothDeviceUIModel.toDomain() : BluetoothDeviceDomainModel {
    return BluetoothDeviceDomainModel(
        name = name,
        address = address
    )
}