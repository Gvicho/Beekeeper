package com.example.beekeeper.presenter.screen.authentication.register

import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.beekeeper.data.common.Resource
import com.example.beekeeper.databinding.FragmentRegistrationBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>(FragmentRegistrationBinding::inflate) {

    private val viewModel: RegistrationViewModel by viewModels()
    override fun setUp() {
        binding.btnRegister.isEnabled = true
    }

    override fun listeners() {
        binding.btnRegister.setOnClickListener{
            viewModel.register(binding.etEmail.text.toString(), binding.etPassword.text.toString(), binding.etRepeatPassword.text.toString())
            bindObservers()
        }

        binding.btnLogIn.setOnClickListener {

        }
    }

    override fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerFlow.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.pbRegister.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            val registeredUser = it.responseData
                            binding.pbRegister.visibility = View.GONE
                            Toast.makeText(requireContext(), "Registration Success", Toast.LENGTH_SHORT).show()
                            binding.btnRegister.isEnabled = false
//                            openLogIn()


                        }

                        is Resource.Failed -> {
                            binding.pbRegister.visibility = View.GONE
                            val errorMessage = it.message
                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT)
                                .show()

                        }
                    }
                }
            }
        }
    }

//    private fun openLogIn() {
//        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
//        findNavController().navigate(action)
//
//    }
//
}