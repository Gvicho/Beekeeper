package com.example.beekeeper.presenter.screen.user

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Log
import android.util.Log.d
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.beekeeper.databinding.FragmentProfileBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.screen.media.ChooseMediaBottomSheetFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel: ProfileViewModel by viewModels()

    override fun setUp() {
        viewModel.readEmail()
        bindObservers()
        getChoice()


    }

    override fun setListeners() {

        binding.ivProfile.setOnClickListener {
            openChooseMediaBottomSheet()

        }

    }

    override fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.emailFlow.collect() {
                    binding.etEmail.setHint(it)

                }
            }
        }
    }

    private fun openChooseMediaBottomSheet() {
        findNavController().navigate(
            ProfileFragmentDirections.actionProfileFragmentToChooseMediaBottomSheetFragment(
                false
            )
        )
    }

    private fun getChoice() {

        setFragmentResultListener("media") { _, bundle ->
            val name = bundle.getString("option")
            d("MEDIASDSFADF", name.toString())

        }

    }

}











