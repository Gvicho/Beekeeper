package com.example.beekeeper.presenter.screen.authentication.reset_password

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentResetPasswordBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.ResetPasswordEvent
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.state.auth.reset_password.ResetPasswordState
import com.example.beekeeper.presenter.utils.SwipeGestureDetector
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding>(FragmentResetPasswordBinding::inflate) {

    private val viewModel: ResetPasswordViewModel by viewModels()
    private lateinit var gestureDetector: GestureDetector
    override fun bind() {
        bindSendButtonClickListener()
    }

    private fun bindSendButtonClickListener(){
        binding.apply {
            btnSendInstructions.setOnClickListener{
                val email = etEmail.text.toString()
                viewModel.onEvent(ResetPasswordEvent.ResetPassword(email))
            }
        }
    }

    override fun bindObservers() {
        bindStateObserver()
        bindNavigationObserver()
    }

    private fun bindNavigationObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.navigationEventFlow.collect{
                    handleNavigation(it)
                }
            }
        }
    }

    private fun handleNavigation(navigationEvent: ResetPasswordViewModel.NavigationEvent){
        when(navigationEvent){
            is ResetPasswordViewModel.NavigationEvent.NavigateBackToLoginPage -> {
                returnResultAndFinishResetProcess(navigationEvent.email)
                closeResetPasswordFragment()
            }
        }
    }

    private fun returnResultAndFinishResetProcess(email: String){
        val result = Bundle().apply {
            putString(getString(R.string.email), email)
        }
        setFragmentResult(getString(R.string.requestkey), result)
    }

    private fun closeResetPasswordFragment() {
        findNavController().popBackStack()
    }

    private fun bindStateObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.resetPasswordPageState.collect{
                    handleResponse(it)
                }
            }
        }
    }

    private fun handleResponse(resetState: ResetPasswordState){
        resetState.errorMessage?.let {
            errorWhileRegistration(it)
        }

        showOrHideProgressBar(resetState.isLoading)
    }

    private fun showOrHideProgressBar(isLoading:Boolean){
        binding.apply {
            pbResetPassword.visibility = if(isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun errorWhileRegistration(errorMessage:String){
        binding.root.showSnackBar(errorMessage)
        viewModel.onEvent(ResetPasswordEvent.ResetErrorMessage)
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