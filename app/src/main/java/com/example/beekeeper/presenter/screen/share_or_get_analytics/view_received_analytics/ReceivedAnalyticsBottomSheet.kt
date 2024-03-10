package com.example.beekeeper.presenter.screen.share_or_get_analytics.view_received_analytics

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.beekeeper.R
import com.example.beekeeper.databinding.BottomSheetReceivedAnalyticsBinding
import com.example.beekeeper.presenter.base_fragment.BaseBottomSheetFragment
import com.example.beekeeper.presenter.event.get_analytics.AnalyticsPreviewEvent
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.model.beehive_analytics.BeehiveAnalyticsUI
import com.example.beekeeper.presenter.state.get_analytics.SaveAnalyticsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReceivedAnalyticsBottomSheet :BaseBottomSheetFragment<BottomSheetReceivedAnalyticsBinding>(BottomSheetReceivedAnalyticsBinding::inflate){

    private val viewModel:ReceivedAnalyticsViewModel by viewModels()

    private lateinit var beehiveAnalytics:BeehiveAnalyticsUI

    override fun bind() {
        receivePassedAnalytics()
        bindSaveBtnListener()
    }

    private fun bindSaveBtnListener(){
        binding.saveBtn.setOnClickListener{
            viewModel.onEvent(AnalyticsPreviewEvent.SaveAnalytics(beehiveAnalytics))
        }
    }

    private fun receivePassedAnalytics(){
        val args: ReceivedAnalyticsBottomSheetArgs by navArgs()
        val id = args.id
        val weightData = args.weightData.toList()
        val temperatureData = args.temperatureData.toList()

        beehiveAnalytics = BeehiveAnalyticsUI(
            id = id,
            weightData = weightData,
            temperatureData = temperatureData
        )

        binding.tvId.text = beehiveAnalytics.id.toString()
    }

    override fun bindObserves() {
        bindDataBaseResponseObserver()
        bindNavigationObservers()
    }

    @OptIn(FlowPreview::class)
    private fun bindNavigationObservers(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.pageNavigationEvent
                    .debounce(1000) // Debounce for 1 second (1000 milliseconds)
                    .collect { event ->
                        handleNavigation(event)
                    }
            }
        }
    }

    private fun handleNavigation(event: ReceivedAnalyticsViewModel.GetAnalyticsPageNavigationEvents){
        when(event){
            is ReceivedAnalyticsViewModel.GetAnalyticsPageNavigationEvents.NavigateBack -> closeBottomSheet()
        }
    }

    private fun bindDataBaseResponseObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.saveAnalyticsState.collect{
                    handleResponse(it)
                }
            }
        }
    }

    private fun handleResponse(receiveState: SaveAnalyticsState){
        receiveState.errorMessage?.let {
            errorWhileRegistration(it)
        }

        showOrHideProgressBar(receiveState.isLoading)

        receiveState.saveStatus?.let {
            binding.root.showSnackBar(getString(R.string.saved))
        }
    }

    private fun showOrHideProgressBar(isLoading:Boolean){
        binding.apply {
            progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun errorWhileRegistration(errorMessage:String){
        binding.root.showSnackBar(errorMessage)
        viewModel.onEvent(AnalyticsPreviewEvent.ResetErrorMessageToNull)
    }

    private fun closeBottomSheet(){
        dismiss()
    }

}