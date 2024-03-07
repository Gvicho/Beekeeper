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
import com.example.beekeeper.presenter.model.damagedBeehives.DamageReportUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DamagedBeehivesFragment :
    BaseFragment<FragmentDamagedBeehivesBinding>(FragmentDamagedBeehivesBinding::inflate) {

    private val viewModel: DamagedBeehivesViewModel by viewModels()

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE) && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(
                    requireContext(),
                    "Permission request denied",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
            }
        }

    private val pickMultipleMedia = registerForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(
            10
        )
    ) { uris ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uris != null) {

            viewModel.uploadImage(uris)
            Log.d("PhotoPicker", "Selected URI: $uris")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }


    override fun setUp() {

    }

    override fun listeners() {

        binding.btnLaunch.setOnClickListener {

            requestPermissions()
        }
    }

    override fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uploadFlow.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.pbUpload.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            val url = it.responseData
                            binding.pbUpload.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "Registration Success",
                                Toast.LENGTH_SHORT
                            ).show()
                            d("FirebaseResultUrl", url)


                        }

                        is Resource.Failed -> {
                            binding.pbUpload.visibility = View.GONE
                            val errorMessage = it.message
                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT)
                                .show()

                        }
                    }
                }
            }
        }
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(arrayOf(Manifest.permission.CAMERA))
    }



}