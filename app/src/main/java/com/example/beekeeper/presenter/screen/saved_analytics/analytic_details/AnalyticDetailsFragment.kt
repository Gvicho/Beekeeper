package com.example.beekeeper.presenter.screen.saved_analytics.analytic_details

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.beekeeper.databinding.FragmentAnalyticDetailsBinding
import com.example.beekeeper.presenter.adapter.charts_recycler.ChartsRecyclerAdapter
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.analytic_details.AnalyticDetailsEvent
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.extension.toDate
import com.example.beekeeper.presenter.model.beehive_analytics.analytics_wrapper.AnalyticsWrapper
import com.example.beekeeper.presenter.state.analytics.AnalyticDetailsUIState
import com.example.beekeeper.presenter.utils.SwipeGestureDetector
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnalyticDetailsFragment : BaseFragment<FragmentAnalyticDetailsBinding>(FragmentAnalyticDetailsBinding::inflate) {

    private val args: AnalyticDetailsFragmentArgs by navArgs()
    private val viewModel:AnalyticsDetailsViewModel by viewModels()
    private lateinit var gestureDetector: GestureDetector
    private lateinit var chartRecyclerAdapter: ChartsRecyclerAdapter
    override fun bind() {
        val beehiveId = args.beehiveId
        viewModel.onEvent(AnalyticDetailsEvent.LoadAnalyticDetails(beehiveId))
    }

    override fun bindObservers() {
        bindStateObserver()
    }

    private fun bindStateObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.beehiveAnalyticDetailsState.collect{
                    handleNewState(it)
                }
            }
        }
    }

    private fun handleNewState(state: AnalyticDetailsUIState){
        state.run {
            errorMessage?.let {
                showErrorMessage(it)
            }

            showOrHideProgressBar(isLoading)

            id?.let {
                binding.tvId.text = it.toString()
            }

            saveTime?.let {
                binding.tvSaveTime.text = it.toDate() // extension fun
            }

            chartList?.let {
                bindAdapterToChartsRecycler(it)
            }
        }
    }

    private fun bindAdapterToChartsRecycler(list:List<AnalyticsWrapper>){
        chartRecyclerAdapter = ChartsRecyclerAdapter(list)
        binding.analyticsRecycler.adapter = chartRecyclerAdapter
    }

    private fun showOrHideProgressBar(isLoading:Boolean){
        binding.apply {
            progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showErrorMessage(errorMessage:String){
        binding.root.showSnackBar(errorMessage)
        viewModel.onEvent(AnalyticDetailsEvent.ResetErrorMessage)
    }


    override fun initSwipeGesture(view: View) {
        val gestureListener = object : SwipeGestureDetector() {


            override fun onSwipeRight() {
                super.onSwipeRight()
                findNavController().popBackStack()
            }
        }

        gestureDetector = GestureDetector(context, gestureListener)
        view.setOnTouchListener { v, event ->
            // Process the gesture detector first
            val gestureDetected = gestureDetector.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    // If no gesture was detected, consider this a click event
                    if (!gestureDetected) {
                        v.performClick()
                    }
                }
            }
            true
        }

    }


}