package com.example.beekeeper.presenter.screen.saved_analytics


import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.beekeeper.databinding.FragmentSavedAnalyticsBinding
import com.example.beekeeper.presenter.adapter.saved_analytics.SavedAnalyticsRecyclerAdapter
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.saved_analytics.SavedAnalyticsEvent
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.state.analytics.SavedAnalyticsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedAnalyticsFragment : BaseFragment<FragmentSavedAnalyticsBinding>(FragmentSavedAnalyticsBinding::inflate),
    SavedAnalyticsRecyclerAdapter.ClickListener {

    private val viewModel: SavedAnalyticsViewModel by viewModels()
    private lateinit var analyticsRecyclerAdapter: SavedAnalyticsRecyclerAdapter

    override fun bind() {
        bindAnalyticsRecyclerAdapter()
    }

    private fun bindAnalyticsRecyclerAdapter(){
        analyticsRecyclerAdapter = SavedAnalyticsRecyclerAdapter(this)
        binding.analyticsRecycler.adapter = analyticsRecyclerAdapter
    }

    override fun bindObservers() {
        bindStateObserver()
    }

    private fun bindStateObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.beehiveAnalyticsState.collect{
                    handleResponse(it)
                }
            }
        }
    }

    private fun handleResponse(savedAnalyticsState: SavedAnalyticsState){
        savedAnalyticsState.errorMessage?.let {
            showErrorMessage(it)
        }

        showOrHideProgressBar(savedAnalyticsState.isLoading)

        savedAnalyticsState.savedBeehiveAnalyticsList?.let {
            analyticsRecyclerAdapter.submitList(it)
        }
    }

    private fun showOrHideProgressBar(isLoading:Boolean){
        binding.apply {
            progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showErrorMessage(errorMessage:String){
        binding.root.showSnackBar(errorMessage)
        viewModel.onEvent(SavedAnalyticsEvent.ResetErrorMessageToNull)
    }

    override fun onClick(id: Int) {
        binding.root.showSnackBar("$id was pressed")
    }

}