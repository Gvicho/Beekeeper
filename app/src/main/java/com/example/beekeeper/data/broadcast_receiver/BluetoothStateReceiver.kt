package com.example.beekeeper.data.broadcast_receiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class BluetoothStateReceiver(
    private val onStateChanged :(isConnected:Boolean, BluetoothDevice) ->Unit
) : BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {

        val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(
                BluetoothDevice.EXTRA_DEVICE,
                BluetoothDevice::class.java
            )
        } else {
            intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        }

        when(intent?.action){
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                onStateChanged(true, device?: return)  // if device is null , it will return onReceive fun without executing on State Change
            }
            BluetoothDevice.ACTION_ACL_DISCONNECTED ->{
                onStateChanged(false, device?: return)
            }
        }
    }

}