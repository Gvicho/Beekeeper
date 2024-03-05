package com.example.beekeeper.presenter.screen.authentication.login

import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.beekeeper.BuildConfig
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentLoginScreenBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.LoginEvent
import com.example.beekeeper.presenter.extension.safeNavigateWithArgs
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.state.auth.login.LoginUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginScreenBinding>(FragmentLoginScreenBinding::inflate) {

    private val viewModel: LogInViewModel by viewModels()

    companion object {
        private const val TEST_EMAIL = "admin@mail.com"
        private const val TEST_PASSWORD = "TBC_2024"
    }

    override fun setUp() {
        super.setUp()
        animations()
    }
    override fun bind() {
        binding.apply {

            btnLogIn.setOnClickListener{
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val rememberedChecked = cbRememberMe.isChecked
                viewModel.onEvent(LoginEvent.LoginUserEvent(email,password,rememberedChecked))
            }

            btnRegister.setOnClickListener{
                viewModel.onEvent(LoginEvent.MoveUserToRegistrationEvent)
            }
        }

        ifOnDebugModeFillFields()

        setRegistrationFragmentResultListener()
    }

    private fun ifOnDebugModeFillFields(){
        if(BuildConfig.DEBUG){
            binding.apply {
                etEmail.setText(TEST_EMAIL)
                etPassword.setText(TEST_PASSWORD)
            }
        }
    }

    private fun handleResponse(loginState: LoginUiState){
        loginState.errorMessage?.let {
            errorWhileRegistration(it)
        }

        showOrHideProgressBar(loginState.isLoading)

        loginState.accessToken?.let {
            successRegistration()
        }
    }

    private fun successRegistration(){
        binding.root.showSnackBar("Successful Login")
    }

    private fun showOrHideProgressBar(isLoading:Boolean){
        binding.apply {
            pbLogIn.visibility = if(isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun errorWhileRegistration(errorMessage:String){
        binding.root.showSnackBar(errorMessage)
        viewModel.onEvent(LoginEvent.ResetErrorStatus)
    }

    private fun setRegistrationFragmentResultListener(){
        parentFragmentManager.setFragmentResultListener("requestKey", this) { _, bundle ->
            val email = bundle.getString("Email")
            val password = bundle.getString("Password")

            binding.etEmail.setText(email)
            binding.etPassword.setText(password)
        }
    }


    override fun bindObservers() {
        bindNavigationEventsObserver()
        bindResponseStateObserver()
    }

    private fun bindNavigationEventsObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.loginPageNavigationEvent.collect{navigationEvent ->
                    when(navigationEvent){
                        LogInViewModel.LoginNavigationEvent.NavigateToHomePageEvent -> navigateToHomePage()
                        LogInViewModel.LoginNavigationEvent.NavigateToRegistrationEvent -> navigateToRegistrationPage()
                    }
                }
            }
        }
    }

    private fun bindResponseStateObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.loginUIState.collect{loginState ->
                    handleResponse(loginState)
                }
            }
        }
    }

    private fun navigateToHomePage(){
        findNavController().safeNavigateWithArgs(R.id.action_loginFragment_to_navigation_home)
    }

    private fun navigateToRegistrationPage(){
        findNavController().safeNavigateWithArgs(R.id.action_loginFragment_to_registrationFragment)
    }


    private fun animations(){
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