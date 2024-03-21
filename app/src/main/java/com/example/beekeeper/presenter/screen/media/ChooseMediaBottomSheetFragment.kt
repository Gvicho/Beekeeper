package com.example.beekeeper.presenter.screen.media

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentChooseMediaBottomSheetBinding
import com.example.beekeeper.presenter.base_fragment.BaseBottomSheetFragment
import androidx.navigation.fragment.navArgs

class ChooseMediaBottomSheetFragment :
    BaseBottomSheetFragment<FragmentChooseMediaBottomSheetBinding>(
        FragmentChooseMediaBottomSheetBinding::inflate
    ) {

    private val navArgs: ChooseMediaBottomSheetFragmentArgs by navArgs()

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in arrayOf(Manifest.permission.CAMERA) && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(
                    requireContext(),
                    "Permission request denied",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                openCameraFragment()
            }
        }

    override fun setUp() {
        getImageResult()
    }


    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                returnChoice(uri.toString())
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
            if (uris.isNotEmpty()) {
                returnMultipleChoice(uris.map {
                    it.toString()
                }.toTypedArray())
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }


    override fun listeners() {
        binding.apply {
            tvCamera.setOnClickListener {
                requestCameraPermission()

            }
            tvGallery.setOnClickListener {
                if (navArgs.isMulti) {
                    pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } else {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }

        }
    }

    private fun getImageResult() {
        setFragmentResultListener("takenPhoto") { _, bundle ->
            val image = bundle.getString("image")
            image?.let {
                if (navArgs.isMulti) {
                    returnMultipleChoice(arrayOf(image))
                } else {
                    returnChoice(image)
                }
            }


        }

    }


    private fun returnChoice(option: String) {
        val result = bundleOf(
            "option" to option
        )
//        d("IMAGERURL", option)
        setFragmentResult("media", result)
        dismiss()
    }

    private fun returnMultipleChoice(images: Array<String>) {
        val result = bundleOf(
            "images" to images
        )
        setFragmentResult("media", result)
        dismiss()
    }


    private fun openCameraFragment() {
        findNavController().navigate(ChooseMediaBottomSheetFragmentDirections.actionChooseMediaBottomSheetFragmentToCameraBottomSheetFragment())
    }

    private fun requestCameraPermission() {
        activityResultLauncher.launch(arrayOf(Manifest.permission.CAMERA))
    }

}