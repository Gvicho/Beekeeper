package com.example.beekeeper.presenter.screen.saved_analytics


import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentSavedAnalyticsBinding
import com.example.beekeeper.domain.utils.Order
import com.example.beekeeper.presenter.adapter.saved_analytics.SavedAnalyticsRecyclerAdapter
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.saved_analytics.SavedAnalyticsEvent
import com.example.beekeeper.presenter.extension.safeNavigate
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.state.analytics.SavedAnalyticsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedAnalyticsFragment : BaseFragment<FragmentSavedAnalyticsBinding>(FragmentSavedAnalyticsBinding::inflate),
    SavedAnalyticsRecyclerAdapter.ClickListener {

    private val viewModel: SavedAnalyticsViewModel by viewModels()
    private lateinit var analyticsRecyclerAdapter: SavedAnalyticsRecyclerAdapter

    override fun loadData() {
        loadIfNotBeenLoaded()
    }

    private fun loadIfNotBeenLoaded(){
        if(viewModel.beehiveAnalyticsState.value.savedBeehiveAnalyticsList == null)
            viewModel.onEvent(SavedAnalyticsEvent.LoadAnalyticsOrder(Order.NONE))
    }

    override fun bind() {
        bindAnalyticsRecyclerAdapter()
        bindOrderSpinnerAdapter()
    }

    override fun setListeners() {
        bindDeleteBtnListener()
        bindUploadBtnListener()
        bindReloadListener()
    }

    private fun bindOrderSpinnerAdapter(){
        binding.orderSpinner.apply {
            val orderList = listOf(
                getString(R.string.none),
                getString(R.string.newer_to_older),
                getString(R.string.older_to_newer)
            )
            val arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,orderList)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapter = arrayAdapter
            setSpinnerSelectListener()
        }
    }

    private fun setSpinnerSelectListener(){
        binding.orderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                val order = when (selectedItem) {
                    requireContext().getString(R.string.newer_to_older) -> {
                        Order.DESCENDING
                    }
                    getString(R.string.older_to_newer) -> {
                        Order.ASCENDING
                    }
                    else -> {
                        Order.NONE
                    }
                }
                viewModel.onEvent(SavedAnalyticsEvent.LoadAnalyticsOrder(order))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.orderSpinner.setSelection(0)
            }
        }
    }

    private fun bindUploadBtnListener(){
        binding.uploadBtn.setOnClickListener{
            viewModel.onEvent(SavedAnalyticsEvent.UploadAnalyticsOnDataBase)
        }
    }

    private fun bindDeleteBtnListener(){
        binding.deleteBtn.setOnClickListener{
            viewModel.onEvent(SavedAnalyticsEvent.DeleteAnalytics)
        }
    }

    private fun bindReloadListener(){
        binding.reloadBtn.setOnClickListener{
            val order = viewModel.beehiveAnalyticsState.value.order
            viewModel.onEvent(SavedAnalyticsEvent.LoadAnalyticsOrder(order))
        }
    }

    private fun bindAnalyticsRecyclerAdapter(){
        analyticsRecyclerAdapter = SavedAnalyticsRecyclerAdapter(this)
        binding.analyticsRecycler.adapter = analyticsRecyclerAdapter
    }

    override fun bindObservers() {
        bindStateObserver()
        bindNavigationObserver()
    }

    private fun bindNavigationObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.savedAnalyticsPageNavigationEvent.collect{
                    handleNavigation(it)
                }
            }
        }
    }

    private fun bindStateObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.beehiveAnalyticsState.collect{
                    handleNewState(it)
                }
            }
        }
    }

    private fun handleNewState(savedAnalyticsState: SavedAnalyticsState){
        savedAnalyticsState.errorMessage?.let {
            showErrorMessage(it)
        }

        showOrHideProgressBar(savedAnalyticsState.isLoading)

        savedAnalyticsState.savedBeehiveAnalyticsList?.let {
            analyticsRecyclerAdapter.submitList(it)
        }

        handleSelectedList(savedAnalyticsState.selectedItemsList)

        savedAnalyticsState.uploadSuccessful?.let {
            showUploadSuccess()
        }
    }

    private fun showUploadSuccess(){
        binding.root.showSnackBar(getString(R.string.upload_was_successful))
        viewModel.onEvent(SavedAnalyticsEvent.ResetUploadSuccessMessageToNull)
    }

    private fun handleSelectedList(selectedIdList: List<Int>){
        showOrHideButtonsLine(selectedIdList.isNotEmpty())
        binding.apply {
            tvSelectedCount.text = selectedIdList.size.toString()
        }
    }

    private fun showOrHideButtonsLine(isLoading:Boolean){
        binding.apply {
            buttonsLine.visibility = if(isLoading) View.VISIBLE else View.GONE
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

    private fun handleNavigation(navEvent: SavedAnalyticsViewModel.SavedAnalyticsNavigationEvents){
        when(navEvent){
            is SavedAnalyticsViewModel.SavedAnalyticsNavigationEvents.NavigateToAnalyticPreviewPage -> navigateToAnalyticDetails(navEvent.beehiveId)
        }
    }

    private fun navigateToAnalyticDetails(beehiveId: Int) {
        val action = SavedAnalyticsFragmentDirections.actionSavedAnalyticsFragmentToAnalyticDetailsFragment(beehiveId)
        findNavController().safeNavigate(action)
    }

    override fun onClick(id: Int) {
        viewModel.onEvent(SavedAnalyticsEvent.OnItemClick(id))
    }

    override fun onLongClick(id: Int) {
        viewModel.onEvent(SavedAnalyticsEvent.OnLongItemClick(id))
    }

}