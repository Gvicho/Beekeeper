package com.example.beekeeper.presenter.screen.home

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.beekeeper.databinding.FragmentHomeBinding
import com.example.beekeeper.presenter.adapter.FarmsRecyclerAdapter
import com.example.beekeeper.presenter.adapter.ItemClickCallBack
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.HomePageEvent
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.model.home.LocationUi
import com.example.beekeeper.presenter.state.home.HomeScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    ItemClickCallBack {

    private lateinit var farmsRecyclerAdapter: FarmsRecyclerAdapter
    private val viewModel:HomeScreenViewModel by viewModels()
    override fun bind() {
        bindFarmsRecycler()
    }

    private fun bindFarmsRecycler(){
        farmsRecyclerAdapter = FarmsRecyclerAdapter(this)
        binding.farmsRecycler.adapter = farmsRecyclerAdapter
    }

    override fun bindObservers() {
        bindStateObserver()
    }

    private fun bindStateObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.homeUIState.collect{
                    handleResponse(it)
                }
            }
        }
    }

    private fun handleResponse(homeScreenState:HomeScreenState){
        homeScreenState.errorMessage?.let {
            showErrorMessage(it)
        }

        showOrHideProgressBar(homeScreenState.isLoading)

        homeScreenState.accessToken?.let {
            farmsRecyclerAdapter.submitList(it)
        }
    }

    private fun showOrHideProgressBar(isLoading:Boolean){
        binding.apply {
            pbLogIn.visibility = if(isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showErrorMessage(errorMessage:String){
        binding.root.showSnackBar(errorMessage)
        viewModel.onEvent(HomePageEvent.ResetErrorMessage)
    }

    override fun onItemClick(id: Int) {
        binding.root.showSnackBar("Open Details Page")
    }

    override fun onLocationButtonClick(location: LocationUi) {
        binding.root.showSnackBar("Open Location on Map")
    }

}