package com.example.beekeeper.presenter.screen.home

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.beekeeper.databinding.FragmentHomeBinding
import com.example.beekeeper.presenter.adapter.home.FarmsRecyclerAdapter
import com.example.beekeeper.presenter.adapter.home.ItemClickCallBack
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.home.HomePageEvent
import com.example.beekeeper.presenter.extension.safeNavigate
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

        homeScreenState.farmList?.let {
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

    private fun openLocationInGoogleMaps(location: LocationUi) {
        val uri = Uri.parse("geo:${location.latitude},${location.longitude}?q=${location.latitude},${location.longitude}(${location.locationName})")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        if (intent.resolveActivity(requireContext().packageManager) != null) {  // from Android 11 (API 30) this is privacy issue. need to add <query> in manifest
            startActivity(intent)
        }
    }

    private fun navigateToDetails(farmId: Int) {
        val action = HomeFragmentDirections.actionNavigationHomeToFarmDetailsFragment(farmId)
        findNavController().safeNavigate(action)
    }

    override fun onItemClick(id: Int) {
        navigateToDetails(id)
    }

    override fun onLocationButtonClick(location: LocationUi) {
        openLocationInGoogleMaps(location)
    }

}