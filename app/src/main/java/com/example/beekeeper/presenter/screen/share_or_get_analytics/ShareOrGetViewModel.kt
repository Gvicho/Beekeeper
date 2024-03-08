package com.example.beekeeper.presenter.screen.share_or_get_analytics

import androidx.lifecycle.ViewModel
import com.example.beekeeper.domain.controller.bluetooth.BluetoothController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareOrGetViewModel @Inject constructor(
    private val bluetoothController: BluetoothController
):ViewModel() {


    override fun onCleared() {
        super.onCleared()
        bluetoothController.release()
    }

}