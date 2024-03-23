package com.example.beekeeper.presenter.screen.get_analytics

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentShareOrGetBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.get_analytics.GetAnalyticsEvent
import com.example.beekeeper.presenter.extension.safeNavigate
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.model.beehive_analytics.BeehiveAnalyticsUI
import com.example.beekeeper.presenter.model.bluetooth_device.BluetoothDeviceUIModel
import com.example.beekeeper.presenter.state.get_analytics.ReceivedBeehiveAnalyticsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShareOrGetFragment : BaseFragment<FragmentShareOrGetBinding>(FragmentShareOrGetBinding::inflate) {
    private val viewModel: ShareOrGetViewModel by viewModels()

    private val bluetoothManager by lazy {
        requireContext().applicationContext.getSystemService(BluetoothManager::class.java) // if in activity requireContext() is not needed
    }


    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter //if device doesn't support bluetooth then this will be null
    } // here is all functionality: get your mac address, name do anything with bluetooth (get paired devices, scanned devices...)

    private val isBluetoothEnabled:Boolean
        get() = bluetoothAdapter?.isEnabled == true

    private var connectedDevice:BluetoothDeviceUIModel? = null

    private val enableBluetoothLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){}
    override fun bind() {
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
        getBooleanResult()
    }

    override fun setUp() {
        setUpBtnAnim()
    }

    private fun getScanResult(){
        // Register the listener
        setFragmentResultListener("device") { key, bundle ->
            // Handle the result here
            val name = bundle.getString("name")
            val address = bundle.getString("address")?:""
            name?.let {
                connectedDevice = BluetoothDeviceUIModel(name = it, address = address) // if name isn't null then mac address is for sure not null
            }
            viewModel.onEvent(GetAnalyticsEvent.HandleInput)
            Log.d("tag123456","received device from scan")
        }
    }

    private fun getBooleanResult() {
        setFragmentResultListener("booleanResultKey") { key, bundle ->
            // Handle the boolean result here
            val booleanResult = bundle.getBoolean("booleanResult")
            if(booleanResult){
                viewModel.onEvent(GetAnalyticsEvent.NavigateToSavedAnalytics)
            }
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
            if(isBluetoothEnabled){
                openScanBottomSheet()
            }else{
                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }
        }
    }

    override fun bindObservers() {
        bindReceivedAnalyticsObserver()
        bindNavigationObservers()
    }

    private fun setUpBtnAnim() {
        val anim = AlphaAnimation(0.7f, 1.0f)
        anim.apply {
            duration = 1200
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
        }
        binding.scanBtn.startAnimation(anim)
    }

    @OptIn(FlowPreview::class)
    private fun bindNavigationObservers(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.pageNavigationEvent
                    .debounce(500)
                    .collect { event ->
                        handleNavigation(event)
                    }
            }
        }
    }

    private fun handleNavigation(event: ShareOrGetViewModel.GetAnalyticsPageNavigationEvents){
        when(event){
            is ShareOrGetViewModel.GetAnalyticsPageNavigationEvents.NavigateToAnalyticsPreviewPage -> openAnalyticsPreview(event.beehiveAnalytics)
            ShareOrGetViewModel.GetAnalyticsPageNavigationEvents.NavigateToSavedAnalyticsPage -> openSaveAnalyticsPage()
        }
    }

    private fun bindReceivedAnalyticsObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.receivedBeehiveAnalyticsState.collect{
                    handleResponse(it)
                }
            }
        }
    }

    private fun handleResponse(receivedAnalyticsState: ReceivedBeehiveAnalyticsState){
        receivedAnalyticsState.errorMessage?.let {
            showError(it)
        }

        showOrHideProgressBar(receivedAnalyticsState.isLoading)

        receivedAnalyticsState.receivedBeehiveAnalytics?.let {
            binding.root.showSnackBar("Success")
        }
    }

    private fun showOrHideProgressBar(isLoading:Boolean){
        binding.apply {
            progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showError(errorMessage:String){
        binding.root.showSnackBar(errorMessage)
        viewModel.onEvent(GetAnalyticsEvent.ResetErrorMessageToNull)
    }

    private fun openScanBottomSheet(){
        findNavController().safeNavigate(R.id.action_shareOrGetFragment_to_scanBottomSheet)
    }

    private fun openSaveAnalyticsPage(){
        findNavController().safeNavigate(R.id.action_shareOrGetFragment_to_savedAnalyticsFragment)
    }

    private fun openAnalyticsPreview(beehiveAnalytics:BeehiveAnalyticsUI){

        val action = ShareOrGetFragmentDirections.actionShareOrGetFragmentToReceivedAnalyticsBottomSheet(
            id = beehiveAnalytics.id,
            weightData = beehiveAnalytics.weightData.toFloatArray(),
            temperatureData = beehiveAnalytics.temperatureData.toFloatArray(),
            saveTime = beehiveAnalytics.saveDateTime
        )

        findNavController().navigate(action)
    }

}