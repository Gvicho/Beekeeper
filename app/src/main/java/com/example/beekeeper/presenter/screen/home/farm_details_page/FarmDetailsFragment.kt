package com.example.beekeeper.presenter.screen.home.farm_details_page

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentFarmDetailsBinding
import com.example.beekeeper.presenter.adapter.home.details.FarmDetailsRecyclerAdapter
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.home.FarmDetailsEvent
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.model.home.LocationUi
import com.example.beekeeper.presenter.model.home.details.FarmDetailsItemWrapper
import com.example.beekeeper.presenter.state.home.FarmDetailsStateUi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FarmDetailsFragment : BaseFragment<FragmentFarmDetailsBinding>(FragmentFarmDetailsBinding::inflate) ,
    FarmDetailsRecyclerAdapter.DetailsListener {

    private val args: FarmDetailsFragmentArgs by navArgs()
    private val viewModel: FarmDetailsViewModel by viewModels()

    private lateinit var detailsRecyclerAdapter: FarmDetailsRecyclerAdapter

    override fun bind() {
        val farmId = args.farmId
        viewModel.onEvent(FarmDetailsEvent.LoadFarmDetailsById(farmId))
    }

    override fun bindObservers() {
        bindStateObservers()
    }

    private fun bindStateObservers(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.farmDetailsState.collect{
                    handleResponse(it)
                }
            }
        }
    }

    private fun handleResponse(state: FarmDetailsStateUi){
        state.errorMessage?.let {
            showErrorMessage(it)
        }

        showOrHideProgressBar(state.isLoading)

        state.farmDetails?.let {
            bindDetailsRecycler(it)
        }
    }

    private fun bindDetailsRecycler(list: List<FarmDetailsItemWrapper>){
        detailsRecyclerAdapter = FarmDetailsRecyclerAdapter(detailsList = list, listener = this)
        binding.detailsRecycler.adapter = detailsRecyclerAdapter
    }

    private fun showOrHideProgressBar(isLoading:Boolean){
        binding.apply {
            progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showErrorMessage(errorMessage:String){
        binding.root.showSnackBar(errorMessage)
        viewModel.onEvent(FarmDetailsEvent.ResetErrorMessage)
    }

    override fun phoneNumberClick(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phone")
        }
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            binding.root.showSnackBar(getString(R.string.no_dialer_app_found))
        }
    }

    override fun mailAddressClicked(mail: String) {
        val gmailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$mail")
            setPackage("com.google.android.gm") // Specify the Gmail package name
        }
        if (gmailIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(gmailIntent)
        } else {
            binding.root.showSnackBar(getString(R.string.no_email_app_found))
        }
    }
    override fun locationClicked(location: LocationUi) {
        val uri = Uri.parse("geo:${location.latitude},${location.longitude}?q=${location.latitude},${location.longitude}(${location.locationName})")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        if (intent.resolveActivity(requireContext().packageManager) != null) {  // from Android 11 (API 30) this is privacy issue. need to add <query> in manifest
            startActivity(intent)
        }
    }

}