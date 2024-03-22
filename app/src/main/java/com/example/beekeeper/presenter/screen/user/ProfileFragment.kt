package com.example.beekeeper.presenter.screen.user

import android.util.Log.d
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentProfileBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.user.ProfilePageEvents
import com.example.beekeeper.presenter.extension.loadImage
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.model.user.UserCredentials
import com.example.beekeeper.presenter.model.user.UserDataUI
import com.example.beekeeper.presenter.state.user.ProfilePageState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel: ProfileViewModel by viewModels()


    override fun setUp() {
        viewModel.onEvent(ProfilePageEvents.ReadUserCredentials)
        bindObservers()
        getChoice()
        bindUserDataObserver()
    }

    override fun setListeners() {
        setChangeProfilePictureBtnListener()
        setSaveBtnListener()
    }

    private fun setChangeProfilePictureBtnListener(){
        binding.changeProfileImageButton.setOnClickListener {
            openChooseMediaBottomSheet()
        }
    }

    private fun setSaveBtnListener(){
        binding.saveBtn.setOnClickListener {
            requestUpload()
        }
    }



    private fun requestUpload(){
        val name = binding.etName.text.toString()
        val lastName = binding.etLastName.text.toString()
        viewModel.onEvent(ProfilePageEvents.SaveNewProfileInfo(name,lastName))
    }

    override fun bindObservers() {
        bindUserCredentialsObserver()
        bindWorkerObservers()
    }

    private fun bindWorkerObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                WorkManager.getInstance(requireContext()).getWorkInfosForUniqueWorkFlow("writeUser")
                    .collect { workInfoList ->
                        workInfoList.forEach { workInfo ->
                            when (workInfo.state) {
                                WorkInfo.State.SUCCEEDED -> {
                                    binding.root.showSnackBar("Info Uploaded")
                                    showOrHideProgressBar(false)
                                }
                                WorkInfo.State.FAILED -> {
                                    val outputData = workInfo.outputData
                                    val errorMessage = outputData.getString("error_message")?:"failed empty error"
                                    binding.root.showSnackBar(errorMessage)
                                    showOrHideProgressBar(false)
                                }
                                WorkInfo.State.RUNNING -> {
                                    showOrHideProgressBar(true)
                                }
                                else -> {
                                    // Handle other states if needed
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun bindUserCredentialsObserver(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userCredentialsFlow.collect {
                    handleUserDetails(it)
                }
            }
        }
    }

    private fun handleUserDetails(userCredentials: UserCredentials){
        userCredentials.token?.let {
            viewModel.onEvent(ProfilePageEvents.RequestCurrentProfileInfo(it))
            d("tag1234","fragment -> token : $it")
        }

        binding.tvEmail.text = userCredentials.mail?:"No Email"
        d("tag1234","fragment -> mail : ${userCredentials.mail}")
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
            val image = bundle.getString("option")
            image?.let {
                handleImage(it)
            }
        }
    }

    private fun handleImage(image:String){
        d("tag1234","fragment image set request")
        viewModel.onEvent(ProfilePageEvents.ImageSelected(image))
    }


    private fun bindUserDataObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userDataFlow.collect {
                    handleState(it)
                }
            }
        }
    }

    private fun handleState(state:ProfilePageState){
        showOrHideProgressBar(state.isLoading)

        state.errorMessage?.let {
            showErrorMessage(it)
        }

        state.profileInfoSaved?.let {
            showUploadSuccessMessage()
        }
        d("tag1234","fragment new State")

        state.userDataUI?.let {
            handleUserInfo(it)
        }
    }

    private fun handleUserInfo(userDataUI: UserDataUI){
        d("tag1234","fragment new UserDataUI")
        binding.tvEmail.text = userDataUI.email
        binding.ivProfile.loadImage(userDataUI.image)
        binding.etName.setText(userDataUI.name)
        binding.etLastName.setText(userDataUI.lastName)
    }

    private fun showOrHideProgressBar(isLoading:Boolean){
        binding.apply {
            progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showErrorMessage(errorMessage:String){
        binding.root.showSnackBar(errorMessage)
        viewModel.onEvent(ProfilePageEvents.ResetErrorMessageToNull)
    }

    private fun showUploadSuccessMessage(){
        binding.root.showSnackBar(getString(R.string.upload_was_successful))
        viewModel.onEvent(ProfilePageEvents.UpdateUploadProfileInfoToNull)
    }



}











