package com.example.beekeeper.presenter.screen.damaged_beehives.report

import android.Manifest
import android.net.Uri
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
import com.example.beekeeper.databinding.DamagePicturesRecyclerItemBinding
import com.example.beekeeper.databinding.FragmentAddReportBinding
import com.example.beekeeper.presenter.adapter.damaged_beehives.DamagePicturesRecyclerAdapter
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.state.damage_report.DamageReportState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddReportFragment :
    BaseFragment<FragmentAddReportBinding>(FragmentAddReportBinding::inflate) {

    private val viewModel: AddReportViewModel by viewModels()
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
                binding.root.showSnackBar("no Images !! ")
            }
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


}