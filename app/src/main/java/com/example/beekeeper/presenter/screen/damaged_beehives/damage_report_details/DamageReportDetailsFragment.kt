package com.example.beekeeper.presenter.screen.damaged_beehives.damage_report_details

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beekeeper.databinding.FragmentDamageReportDetailsBinding
import com.example.beekeeper.presenter.adapter.damaged_beehives.DamagePicturesRecyclerAdapter
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.damage_beehives.DamageReportDetailsPageEvent
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.state.damage_report.details.DamageReportDetailsPageState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DamageReportDetailsFragment :
    BaseFragment<FragmentDamageReportDetailsBinding>(FragmentDamageReportDetailsBinding::inflate) {

    private val viewModel: DamageReportDetailsViewModel by viewModels()

    private val args: DamageReportDetailsFragmentArgs by navArgs()
    private lateinit var damagePicturesRecyclerAdapter: DamagePicturesRecyclerAdapter

    override fun setUp() {
        initRecycler()
        viewModel.onEvent(DamageReportDetailsPageEvent.GetReportDetailsEvent(args.id))
        bindDetailsStateObserver()


    }

    private fun initRecycler() {
        damagePicturesRecyclerAdapter = DamagePicturesRecyclerAdapter()
        binding.apply {
            damagedBeehiveImagesRecycler.adapter = damagePicturesRecyclerAdapter
            damagedBeehiveImagesRecycler.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

    }


    private fun bindDetailsStateObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reportFlow.collect {
                    handState(it)
                }
            }
        }
    }

    private fun handState(state: DamageReportDetailsPageState){
        showOrHideProgressBar(state.isLoading)

        state.reportDetails?.let {res->
            binding.apply {
                tvId.text = res.id.toString()
                tvDate.text = res.dateUploaded
                tvDamageLvl.text = res.damageLevelIndicator.toString()
                tvDesc.text = res.damageDescription
            }
            damagePicturesRecyclerAdapter.submitList(res.imageUris)
        }

        state.errorMessage?.let{
            showErrorMessage(it)
        }
    }

    private fun showOrHideProgressBar(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showErrorMessage(errorMessage: String) {
        binding.root.showSnackBar(errorMessage)
        viewModel.onEvent(DamageReportDetailsPageEvent.ResetErrorMessage)
    }

}