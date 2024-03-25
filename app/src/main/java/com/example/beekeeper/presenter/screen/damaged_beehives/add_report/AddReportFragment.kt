package com.example.beekeeper.presenter.screen.damaged_beehives.add_report

import android.net.Uri
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beekeeper.databinding.FragmentAddReportBinding
import com.example.beekeeper.presenter.adapter.damaged_beehives.DamagePicturesRecyclerAdapter
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.damage_beehives.AddReportPageEvents
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.state.damage_report.DamageDescriptionState
import com.example.beekeeper.presenter.state.worker_states.WorkerStatusState
import com.example.beekeeper.presenter.utils.SwipeGestureDetector
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddReportFragment :
    BaseFragment<FragmentAddReportBinding>(FragmentAddReportBinding::inflate) ,
    DamagePicturesRecyclerAdapter.ImagesRemoveListener {

    private val viewModel: AddReportViewModel by viewModels()
    private lateinit var gestureDetector: GestureDetector
    private lateinit var damagePicturesAdapter: DamagePicturesRecyclerAdapter


    override fun bindObservers() {
        bindImagesListObserver()
        bindWorkerResultObserver()

    }

    private fun bindWorkerResultObserver(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.workStatus.collect {
                    handleWorkerStatusState(it)
                }
            }
        }
    }

    private fun handleWorkerStatusState(state: WorkerStatusState){

        showOrHideProgressBar(state.isLoading)
        enableDisableUploadBtn(!state.isLoading) //upload button will be disabled

        state.failedMessage?.let {
            showErrorMessage(it)
            viewModel.onEvent(AddReportPageEvents.ResetFailToNull)
        }

        state.blocked?.let {

            viewModel.onEvent(AddReportPageEvents.ResetBlockToNull)
        }

        state.wasCanceled?.let {

            viewModel.onEvent(AddReportPageEvents.ResetCancelToNull)
        }

        state.uploadedSuccessfully?.let {
            viewModel.onEvent(AddReportPageEvents.ResetSuccessToNull)
            disable()
        }

    }
    private fun disable(){
        findNavController().popBackStack()
    }


    private fun bindImagesListObserver(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.imagesList.collect {
                    damagePicturesAdapter.submitList(it.images)
                }
            }
        }
    }



    private fun showOrHideProgressBar(isLoading: Boolean) {
        binding.apply {
            pbUpload.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun enableDisableUploadBtn(enable:Boolean){
        binding.btnUpload.isEnabled = enable
    }

    private fun showErrorMessage(errorMessage: String) {
        binding.root.showSnackBar(errorMessage)
    }

    override fun setUp() {
        initRecycler()
        getChoice()
    }

    override fun setListeners() {
        binding.btnPick.setOnClickListener {
            openChooseMedia()
        }

        binding.btnUpload.setOnClickListener {
            viewModel.onEvent(AddReportPageEvents.UploadReport(
                desc = binding.etDescription.text.toString(),
                damageLevel = binding.sbDamageLevel.progress
            ))
        }
        binding.btnGenerateDesc.setOnClickListener {
            viewModel.onEvent(AddReportPageEvents.GetDescription)
            bindGeneratedDesc()
        }

    }



    private fun initRecycler() {
        damagePicturesAdapter = DamagePicturesRecyclerAdapter(this)
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
                    handleGeneratedDescState(it)
                }
            }
        }
    }

    private fun handleGeneratedDescState(state:DamageDescriptionState){
        showOrHideProgressBar(state.isLoading)

        state.description?.let {
            binding.etDescription.setText(it)
            viewModel.onEvent(AddReportPageEvents.ResetErrorMessageOfDescriptionToNull)
        }

        state.errorMessage?.let{
            showErrorMessage(it)

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

    private  fun openChooseMedia(){
        findNavController().navigate(AddReportFragmentDirections.actionAddReportFragmentToChooseMediaBottomSheetFragment(true))
    }


    private fun getChoice() {

        setFragmentResultListener("media") { _, bundle ->
            val images = bundle.getStringArray("images")
            images?.let {
                val newImages = mutableListOf<Uri>()
                for (each in images){
                    newImages.add(each.toUri())
                }
                viewModel.onEvent(AddReportPageEvents.AddImagesToList(newImages))
            }
        }

    }

    override fun onRemoveImage(uri: Uri) {
        viewModel.onEvent(AddReportPageEvents.RemoveImageFromList(uri))
    }

}