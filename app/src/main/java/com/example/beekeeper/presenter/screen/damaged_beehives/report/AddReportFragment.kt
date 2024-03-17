package com.example.beekeeper.presenter.screen.damaged_beehives.report

import android.Manifest
import android.net.Uri
import android.util.Log.d
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beekeeper.databinding.FragmentAddReportBinding
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.presenter.adapter.damaged_beehives.DamagePicturesRecyclerAdapter
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.state.damage_report.DamageReportState
import com.example.beekeeper.presenter.utils.SwipeGestureDetector
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddReportFragment :
    BaseFragment<FragmentAddReportBinding>(FragmentAddReportBinding::inflate) {

    private val viewModel: AddReportViewModel by viewModels()
    private lateinit var gestureDetector: GestureDetector
    private lateinit var damagePicturesAdapter: DamagePicturesRecyclerAdapter
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
    private var uriList = listOf<Uri>()

    private val pickMultipleMedia = registerForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(
            10
        )
    ) { uris ->
        uris?.let {
            uriList = it
            binding.root.showSnackBar("added ${it.size}")
            damagePicturesAdapter.submitList(uris)
        }
    }

    override fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reportUIState.collect {
                    handleReportState(it)
                }
            }
        }
    }

    private fun handleReportState(state: DamageReportState) {

        showOrHideProgressBar(state.isLoading)

        state.uploadSuccess?.let {
            findNavController().popBackStack()
        }

    }

    private fun showOrHideProgressBar(isLoading: Boolean) {
        binding.apply {
            pbUpload.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun setUp() {

        initRecycler()

    }

    override fun setListeners() {
        binding.btnPick.setOnClickListener {
            requestPermissions()
        }

        binding.btnUpload.setOnClickListener {
            if (uriList.isNotEmpty()) {
                viewModel.uploadReport(
                    desc = binding.etDescription.text.toString(),
                    damageLevel = binding.sbDamageLevel.progress,
                    uriList
                )
                binding.root.showSnackBar("start of sending")
            } else {
                binding.root.showSnackBar("no Images !!")
            }
        }
        binding.btnGenerateDesc.setOnClickListener {

        }

    }

    private fun requestPermissions() {
        activityResultLauncher.launch(arrayOf(Manifest.permission.CAMERA))
    }

    private fun initRecycler() {
        damagePicturesAdapter = DamagePicturesRecyclerAdapter()
        binding.apply {
            uploadedPicturesRecycler.adapter = damagePicturesAdapter
            uploadedPicturesRecycler.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun bindGeneratedDesc() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.descFlow.collect {
                    when (it) {
                        is Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            val res = it.responseData
                            d("ResultDescription", res)
                            binding.etDescription.setText(res)


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

    override fun initSwipeGesture(view: View) {
        val gestureListener = object : SwipeGestureDetector() {


            override fun onSwipeRight() {
                super.onSwipeRight()
                findNavController().popBackStack()
            }
        }

        gestureDetector = GestureDetector(context, gestureListener)
        view.setOnTouchListener { v, event ->
            // Process the gesture detector first
            val gestureDetected = gestureDetector.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    // If no gesture was detected, consider this a click event
                    if (!gestureDetected) {
                        v.performClick()
                    }
                }
            }
            true
        }

    }



}