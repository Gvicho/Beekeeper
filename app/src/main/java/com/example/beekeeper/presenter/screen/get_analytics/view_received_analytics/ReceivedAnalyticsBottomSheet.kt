package com.example.beekeeper.presenter.screen.get_analytics.view_received_analytics

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.beekeeper.R
import com.example.beekeeper.databinding.BottomSheetReceivedAnalyticsBinding
import com.example.beekeeper.presenter.adapter.charts_recycler.ChartsRecyclerAdapter
import com.example.beekeeper.presenter.base_fragment.BaseBottomSheetFragment
import com.example.beekeeper.presenter.event.get_analytics.AnalyticsPreviewEvent
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.mappers.beehive_analytics.toChartData
import com.example.beekeeper.presenter.model.beehive_analytics.BeehiveAnalyticsUI
import com.example.beekeeper.presenter.model.beehive_analytics.analytics_wrapper.AnalyticsWrapper
import com.example.beekeeper.presenter.state.get_analytics.SaveAnalyticsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReceivedAnalyticsBottomSheet :BaseBottomSheetFragment<BottomSheetReceivedAnalyticsBinding>(BottomSheetReceivedAnalyticsBinding::inflate){

    private val viewModel:ReceivedAnalyticsViewModel by viewModels()

    private lateinit var beehiveAnalytics:BeehiveAnalyticsUI

    private lateinit var chartRecyclerAdapter: ChartsRecyclerAdapter

    override fun bind() {
        receivePassedAnalytics()
        bindSaveBtnListener()
    }

    private fun bindAdapterToChartsRecycler(list:List<AnalyticsWrapper>){
        chartRecyclerAdapter = ChartsRecyclerAdapter(list)
        binding.analyticsRecycler.adapter = chartRecyclerAdapter
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
        val saveTime = args.saveTime

        beehiveAnalytics = BeehiveAnalyticsUI(
            id = id,
            weightData = weightData,
            temperatureData = temperatureData,
            saveDateTime = saveTime
        )

        val chartsList = beehiveAnalytics.toChartData()
        binding.tvId.text = beehiveAnalytics.id.toString()
        bindAdapterToChartsRecycler(chartsList)
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
        binding.errorViewHolder.showSnackBar(errorMessage)
        viewModel.onEvent(AnalyticsPreviewEvent.ResetErrorMessageToNull)
    }
     private fun setResult(){
         // In the new fragment, when you need to set the result
         val result = Bundle().apply {
             putBoolean("booleanResult", true) // Or false, depending on your logic
         }
         setFragmentResult("booleanResultKey", result)
     }

    private fun closeBottomSheet(){
        setResult()
        dismiss()
    }

}