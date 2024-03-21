package com.example.beekeeper.presenter.screen.user

import android.util.Log.d
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.beekeeper.databinding.FragmentProfileBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.user.ProfilePageEvents
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel: ProfileViewModel by viewModels()

    override fun setUp() {
        viewModel.onEvent(ProfilePageEvents.ReadUserEmailFromDataStore)
        bindObservers()
        getChoice()
    }

    override fun setListeners() {

        binding.changeProfileImageButton.setOnClickListener {
            openChooseMediaBottomSheet()

        }

    }

    override fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.emailFlow.collect{
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
            val name = bundle.getString("option")
            d("MEDIASDSFADF", name.toString())

        }

    }

}











