package com.example.beekeeper.presenter.screen.share_or_get_analytics

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentShareOrGetBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.extension.safeNavigate
import com.example.beekeeper.presenter.extension.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareOrGetFragment : BaseFragment<FragmentShareOrGetBinding>(FragmentShareOrGetBinding::inflate) {
    private val viewModel: ShareOrGetViewModel by viewModels()

    private val bluetoothManager by lazy {
        requireContext().applicationContext.getSystemService(BluetoothManager::class.java) // if in activity requireContext() is not nececery
    }


    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter //if device doesn't support bluetooth then this will be null
    } // where is all functionality: get your mac address, name do anything with bluetooth (get paired devices, scanned devices...)

    private val isBluetoothEnabled:Boolean
        get() = bluetoothAdapter?.isEnabled == true


    override fun bind() {

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

            if(!canEnableBluetooth){
                disableScannerBtn() // if bluetooth permission is not granted don't allow user for scan access
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
            arrayOf() // Or specific permissions your app requires on older Android versions
        }

        // Request the permissions
        if (requiredPermissions.isNotEmpty()) {
            permissionLauncher.launch(requiredPermissions)
        }

        getScanResult()

    }

    private fun getScanResult(){

        // Register the listener
        setFragmentResultListener("device") { key, bundle ->
            // Handle the result here
            val name = bundle.getString("name")
            val address = bundle.getString("address")
            binding.root.showSnackBar("Connected to $name")
        }

    }

    private fun disableScannerBtn(){
        binding.apply {
            scanBtn.isEnabled = false
            tvServiceNotAvailable.visibility = View.VISIBLE
        }
    }

    override fun setListeners() {
        bindScanButton()
    }

    private fun bindScanButton(){
        binding.scanBtn.setOnClickListener{
            openScanBottomSheet()
        }
    }

    private fun openScanBottomSheet(){
        findNavController().safeNavigate(R.id.action_shareOrGetFragment_to_scanBottomSheet)
    }



}