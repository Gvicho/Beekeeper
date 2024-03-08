package com.example.beekeeper.presenter.screen.damaged_beehives

import androidx.navigation.fragment.findNavController
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentDamagedBeehivesBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.extension.safeNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DamagedBeehivesFragment :
    BaseFragment<FragmentDamagedBeehivesBinding>(FragmentDamagedBeehivesBinding::inflate) {

    override fun setUp() {

    }

    override fun setListeners() {
        binding.btnLaunch.setOnClickListener {
            openAddReportFragment()
        }
    }



    private fun openAddReportFragment() {
        findNavController().safeNavigate(R.id.action_damagedBeehivesFragment_to_addReportFragment)
    }


}