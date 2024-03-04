package com.example.beekeeper.presenter.screen.authentication.login

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.beekeeper.R
import com.example.beekeeper.data.common.Resource
import com.example.beekeeper.databinding.FragmentLoginScreenBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginScreenBinding>(FragmentLoginScreenBinding::inflate) {

    private val viewModel: LogInViewModel by viewModels()


    override fun setUp() {

        binding.btnLogIn.isEnabled = true
        anims()

    }

    override fun listeners() {
        binding.btnLogIn.setOnClickListener {
            viewModel.logIn(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            bindObservers()
        }

        binding.btnRegister.setOnClickListener {
//            openRegister()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })

        binding.tvRecoverPassword.setOnClickListener{
//            openRecover()
        }

    }

    override fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.logInFlow.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.pbLogIn.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            val activeUser = it.responseData

                            binding.pbLogIn.visibility = View.GONE
                            Toast.makeText(requireContext(), "Log In  Success", Toast.LENGTH_SHORT).show()


                        }

                        is Resource.Failed -> {
                            binding.pbLogIn.visibility = View.GONE
                            val errorMessage = it.message
                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT)
                                .show()

                        }
                    }
                }
            }
        }
    }

//    private fun openHome() {
//        val action = LogInFragmentDirections.actionLoginFragmentToHomeFragment()
//        findNavController().navigate(action)
//    }
//
//    private fun openRecover() {
//        val action = LogInFragmentDirections.actionLoginFragmentToRecoverPasswordFragment()
//        findNavController().navigate(action)
//    }
//
//    private fun openRegister() {
//        val action = LogInFragmentDirections.actionLoginFragmentToRegisterFragment()
//        findNavController().navigate(action)
//    }


    private fun anims(){
        val slideDownAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down)
        val slideInRight = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
        val slideInLeft = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_left)
        val slideInBottom = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_bottom)
        binding.apply {
            ivSemiCircle.startAnimation(slideDownAnimation)
            etEmail.startAnimation(slideInLeft)
            etPassword.startAnimation(slideInRight)
            cbRememberMe.startAnimation(slideInLeft)
            btnLogIn.startAnimation(slideInRight)
            btnRegister.startAnimation(slideInLeft)
            tvRecoverPassword.startAnimation(slideInBottom)
        }
    }
}