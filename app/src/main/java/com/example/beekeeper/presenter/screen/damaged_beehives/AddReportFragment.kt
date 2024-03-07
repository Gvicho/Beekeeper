package com.example.beekeeper.presenter.screen.damaged_beehives

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentAddReportBinding
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddReportFragment : BaseFragment<FragmentAddReportBinding>(FragmentAddReportBinding::inflate) {

    private val viewModel: AddReportViewModel by viewModels()
    private val imagesHolder = mutableListOf<Uri>()
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
                pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }

    private val pickMultipleMedia = registerForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(
            10
        )
    ) { uris ->

        if (uris != null) {

            viewModel.uploadImage(uris)

        } else {
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
                            Log.d("FirebaseResultUrl", url)


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



    override fun setUp() {

    }

    override fun listeners() {
    binding.btnLaunch.setOnClickListener {
        requestPermissions()
    }

    }

    private fun requestPermissions() {
        activityResultLauncher.launch(arrayOf(Manifest.permission.CAMERA))
    }
}