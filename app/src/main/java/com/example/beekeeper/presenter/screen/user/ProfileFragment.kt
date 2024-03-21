package com.example.beekeeper.presenter.screen.user

import android.util.Log.d
import android.view.View
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.beekeeper.databinding.FragmentProfileBinding
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.user.ProfilePageEvents
import com.example.beekeeper.presenter.extension.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel: ProfileViewModel by viewModels()

    override fun setUp() {
        viewModel.onEvent(ProfilePageEvents.ReadUserEmailFromDataStore)
        bindObservers()
        getChoice()
        viewModel.getUserData()
        bindUserData()
    }

    override fun setListeners() {

        binding.changeProfileImageButton.setOnClickListener {
            openChooseMediaBottomSheet()

        }

    }

    override fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.emailFlow.collect {
                    binding.tvEmail.text = it

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
            val image = bundle.getString("option")
            image?.let {
                viewModel.writeUserData(it)
            }


        }

    }


    fun bindUserData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userDataFlow.collect() {
                    when (it) {

                        is Resource.Loading -> {
                        }

                        is Resource.Success -> {
                            val res = it.responseData
                            binding.apply {
                                tvEmail.text = res.email
                                etLastName.setText(res.lastName)
                                etName.setText(res.name)
                                ivProfile.loadImage(res.image)
                            }
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


}











