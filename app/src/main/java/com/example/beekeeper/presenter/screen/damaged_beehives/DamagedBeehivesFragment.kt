package com.example.beekeeper.presenter.screen.damaged_beehives

import android.util.Log.d
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentDamagedBeehivesBinding
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.presenter.adapter.damaged_beehives.ReportsRecyclerAdapter
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.extension.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DamagedBeehivesFragment :
    BaseFragment<FragmentDamagedBeehivesBinding>(FragmentDamagedBeehivesBinding::inflate) {

    private val viewModel: DamagedBeehivesViewModel by viewModels()
    private lateinit var reportsAdapter: ReportsRecyclerAdapter

    override fun setUp() {
        initRecycler()
        viewModel.getReports()
        bindObservers()
    }

    override fun setListeners() {
        binding.btnLaunch.setOnClickListener {
            openAddReportFragment()
        }
    }


    private fun openAddReportFragment() {
        findNavController().safeNavigate(R.id.action_damagedBeehivesFragment_to_addReportFragment)
    }

    override fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reportsFlow.collect() {
                    when (it) {

                        is Resource.Loading -> {
                            binding.pbReports.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            binding.pbReports.visibility = View.GONE
                            val res = it.responseData
                            reportsAdapter.submitList(res)

                            d("DamageReports", "$res")

                        }

                        is Resource.Failed -> {
                            binding.pbReports.visibility = View.GONE
                            val errorMessage = it.message
                            d("DamageReports", errorMessage)

                        }

                    }
                }
            }
        }

    }

    private fun initRecycler(){
        reportsAdapter = ReportsRecyclerAdapter()
        binding.apply {
            reportsRecyclerView.adapter = reportsAdapter
            reportsRecyclerView.layoutManager =   LinearLayoutManager(requireContext())

        }

    }


}