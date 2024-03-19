package com.example.beekeeper.presenter.screen.share_or_get_analytics.scan

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.beekeeper.databinding.BottomSheetScanConnectBinding
import com.example.beekeeper.presenter.adapter.scan_list_recycler.BluetoothDevicesRecyclerAdapter
import com.example.beekeeper.presenter.base_fragment.BaseBottomSheetFragment
import com.example.beekeeper.presenter.event.get_analytics.ScanEvent
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.model.bluetooth_device.BluetoothDeviceUIModel
import com.example.beekeeper.presenter.state.bluetooth_beehive.ScanPageUIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScanBottomSheet :BaseBottomSheetFragment<BottomSheetScanConnectBinding>(BottomSheetScanConnectBinding::inflate),
    BluetoothDevicesRecyclerAdapter.ClickListener {

    private val viewModel: ScanViewModel by viewModels()

    private lateinit var pairedDevicesRecyclerAdapter :BluetoothDevicesRecyclerAdapter
    private lateinit var scannedDevicesRecyclerAdapter :BluetoothDevicesRecyclerAdapter

    private val bluetoothManager by lazy {
        requireContext().applicationContext.getSystemService(BluetoothManager::class.java) // if in activity requireContext() is not needed
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter //if device doesn't support bluetooth then this will be null
    } // where is all functionality: get your mac address, name do anything with bluetooth (get paired devices, scanned devices...)

    private val isBluetoothEnabled:Boolean
        get() = bluetoothAdapter?.isEnabled == true

    private var navigationFlag = true


    override fun bind() {
        pairedDevicesRecyclerAdapter = BluetoothDevicesRecyclerAdapter(this)
        scannedDevicesRecyclerAdapter = BluetoothDevicesRecyclerAdapter(this)

        val enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){}

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ){permissions->
            // here we handle permissions
            val canEnableBluetooth = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                permissions[Manifest.permission.BLUETOOTH_CONNECT] == true
            }else{
                true
            }

            if (canEnableBluetooth && !isBluetoothEnabled){
                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }
        }

        // Define the permissions based on the Android version
        val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            // Before Android 12, you might not need to request permissions at runtime for Bluetooth
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION) // Or specific permissions your app requires on older Android versions
        }

        // Request the permissions
        if (requiredPermissions.isNotEmpty()) {
            permissionLauncher.launch(requiredPermissions)
        }

        setAdapterToPairedDevicesRecycler()
        setAdapterToScannedDevicesRecycler()

        viewModel.onEvent(ScanEvent.StartScan)
    }

    private fun setAdapterToPairedDevicesRecycler(){
        binding.pairedDevicesRecycler.adapter = pairedDevicesRecyclerAdapter
    }

    private fun setAdapterToScannedDevicesRecycler(){
        binding.scannedDevicesRecycler.adapter = scannedDevicesRecyclerAdapter
    }

    private var resultDevice : BluetoothDeviceUIModel? = null
    override fun bindObserves() {
        bindStateObserver()
    }
    private fun bindStateObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.state.collect{
                    handleUiState(it)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun handleUiState(state:ScanPageUIState){

        state.errorMessage?.let {
            showErrorMessage(it)
            viewModel.onEvent(ScanEvent.ResetErrorMessageToNull)
        }

        showLoader(state.isConnecting)

        if(state.isConnected){
            if(navigationFlag)returnPickedDevice()
        }

        if(state.pairedDevices.isNotEmpty()){
            pairedDevicesRecyclerAdapter.submitList(state.pairedDevices)
        }

        if(state.scannedDevices.isNotEmpty()){
            scannedDevicesRecyclerAdapter.submitList(state.scannedDevices)
        }
    }

    private fun returnPickedDevice(){
        navigationFlag = false
        val result = bundleOf(
            "name" to (resultDevice?.name?:""),
            "address" to (resultDevice?.address?:"")
        )
        setFragmentResult("device", result)
        dismiss()
    }

    private fun showErrorMessage(errorMessage:String){
        binding.errorViewHolder.showSnackBar(errorMessage)
    }

    private fun showLoader(loading:Boolean){
        binding.progressBar.visibility = if(loading) View.VISIBLE else View.GONE
    }

    override fun onClick(device: BluetoothDeviceUIModel) {
        resultDevice = device
        viewModel.onEvent(ScanEvent.ConnectToDevice(device))
    }

    override fun onPause() {
        super.onPause()
        viewModel.onEvent(ScanEvent.StopScan)
    }
}

