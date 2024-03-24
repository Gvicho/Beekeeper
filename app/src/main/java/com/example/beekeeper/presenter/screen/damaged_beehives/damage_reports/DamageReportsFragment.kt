package com.example.beekeeper.presenter.screen.damaged_beehives.damage_reports

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentDamagedBeehivesBinding
import com.example.beekeeper.presenter.adapter.damaged_beehives.ReportsRecyclerAdapter
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.damage_beehives.DamagedBeehivePageEvents
import com.example.beekeeper.presenter.extension.safeNavigate
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.state.damage_report.DamagedBeehivePageState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DamageReportsFragment :
    BaseFragment<FragmentDamagedBeehivesBinding>(FragmentDamagedBeehivesBinding::inflate) {

    private val viewModel: DamageReportsViewModel by viewModels()
    private lateinit var reportsAdapter: ReportsRecyclerAdapter

    override fun setUp() {
        bindRecycler()
        viewModel.onEvent(DamagedBeehivePageEvents.GetReports)
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
                viewModel.currentReportsFlow.collect {
                    handleState(it)
                }
            }
        }

    }

    private fun handleState(state: DamagedBeehivePageState) {

        state.errorMessage?.let {
            showErrorMessage(it)
        }

        showOrHideProgressBar(state.isLoading)

        state.currentReportsList?.let {
            reportsAdapter.submitList(it)
        }

    }

    private fun showOrHideProgressBar(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showErrorMessage(errorMessage: String) {
        binding.root.showSnackBar(errorMessage)
        viewModel.onEvent(DamagedBeehivePageEvents.ResetErrorMessageToNull)
    }

    private fun bindRecycler() {
        reportsAdapter = ReportsRecyclerAdapter {
            openDetails(it.id)
        }
        binding.apply {
            reportsRecyclerView.adapter = reportsAdapter
            reportsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun openDetails(id: Int) {
        findNavController().navigate(
            DamageReportsFragmentDirections.actionDamagedBeehivesFragmentToDamageReportDetailsFragment(
                id
            )
        )
    }


}