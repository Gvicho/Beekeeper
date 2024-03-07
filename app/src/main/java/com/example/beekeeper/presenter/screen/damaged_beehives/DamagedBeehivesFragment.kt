package com.example.beekeeper.presenter.screen.damaged_beehives

import android.Manifest
import android.util.Log
import android.util.Log.d
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.beekeeper.databinding.FragmentDamagedBeehivesBinding
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DamagedBeehivesFragment :
    BaseFragment<FragmentDamagedBeehivesBinding>(FragmentDamagedBeehivesBinding::inflate) {

    override fun setUp() {

    }

    override fun listeners() {

        binding.btnLaunch.setOnClickListener {

            openAddReportFragment()
        }
    }



    private fun openAddReportFragment() {
        findNavController().navigate(DamagedBeehivesFragmentDirections.actionDamagedBeehivesFragmentToAddReportFragment())
    }


}