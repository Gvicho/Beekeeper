package com.example.beekeeper.presenter.screen.authentication.register

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentRegistrationBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.RegisterEvent
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.state.auth.register.RegisterState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>(FragmentRegistrationBinding::inflate) {

    private val viewModel:RegistrationViewModel by viewModels()

    override fun bind() {
        animations()
    }

    override fun bindObservers() {
        observeRegistrationState()
        observeNavigationEvent()
    }

    private fun observeRegistrationState(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.registrationState.collect{
                    handleResponse(it)
                }
            }
        }
    }

    private fun observeNavigationEvent(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.navigationEventFlow.collect{
                    handleNavigation(it)
                }
            }
        }
    }

    private fun handleNavigation(navigationEvent: RegistrationViewModel.NavigationEvent){
        when(navigationEvent){
            is RegistrationViewModel.NavigationEvent.NavigateBackToLoginPage -> {
                returnResultAndFinishRegistration(navigationEvent.email,navigationEvent.password)
                closeRegistrationFragment()
            }
        }
    }

    private fun returnResultAndFinishRegistration(email: String,password: String){
        val result = Bundle().apply {
            putString(getString(R.string.email), email)
            putString(getString(R.string.password), password)
        }
        setFragmentResult(getString(R.string.requestkey), result)
    }

    private fun closeRegistrationFragment() {
        findNavController().popBackStack()
    }

    override fun bindViewActionListeners() {
        binding.apply {
            btnRegister.setOnClickListener{
                registerUser()
            }
        }
    }

    private fun handleResponse(registerState: RegisterState){
        registerState.errorMessage?.let {
            errorWhileRegistration(it)
        }

        showOrHideProgressBar(registerState.isLoading)

        registerState.userAuthenticator?.let {
            successRegistration(it.email,it.password)
        }
    }

    private fun successRegistration(email:String,password: String){
        binding.root.showSnackBar(getString(R.string.successful_registration))
        viewModel.onEvent(RegisterEvent.MoveBackToLogin(email,password))
    }

    private fun showOrHideProgressBar(isLoading:Boolean){
        binding.apply {
            pbRegister.visibility = if(isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun errorWhileRegistration(errorMessage:String){
        binding.root.showSnackBar(errorMessage)
        viewModel.onEvent(RegisterEvent.ResetErrorMessage)
    }

    private fun registerUser(){
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val repeatedPassword = binding.etRepeatPassword.text.toString()

        requestRegistrationToViewModel(email,password,repeatedPassword)
    }

    private fun requestRegistrationToViewModel(email: String,password: String,repeatedPassword:String){
        viewModel.onEvent(RegisterEvent.RegisterUser(email,password,repeatedPassword))
    }

    private fun animations(){
        val slideInRight = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
        val slideInLeft = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_left)
        binding.apply {
            etEmail.startAnimation(slideInLeft)
            etPassword.startAnimation(slideInRight)
            etRepeatPassword.startAnimation(slideInLeft)
            btnRegister.startAnimation(slideInRight)
        }
    }
}