package com.example.beekeeper.presenter.screen.damaged_beehives.damage_report_details

import android.util.Log.d
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beekeeper.databinding.FragmentDamageReportDetailsBinding
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.presenter.adapter.damaged_beehives.DamagePicturesRecyclerAdapter
import com.example.beekeeper.presenter.base_fragment.BaseFragment
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
        viewModel.getReportById(args.id)
        bindDetails()


    }

    override fun setListeners() {

    }


    private fun initRecycler() {
        damagePicturesRecyclerAdapter = DamagePicturesRecyclerAdapter()
        binding.apply {
            damagedBeehiveImagesRecycler.adapter = damagePicturesRecyclerAdapter
            damagedBeehiveImagesRecycler.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

    }


    private fun bindDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reportFlow.collect {
                    when (it) {
                        is Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            val res = it.responseData
                            binding.apply {
                                tvId.text = res.id.toString()
                                tvDate.text = res.dateUploaded
                                tvDamageLvl.text = res.damageLevelIndicator.toString()
                                tvDesc.text = res.damageDescription
                            }
                            damagePicturesRecyclerAdapter.submitList(res.imageUris)
                            d("DetailsOFReport", res.toString())


                        }

                        is Resource.Failed -> {
                            val errorMessage = it.message
                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

}