package com.example.beekeeper.presenter.screen.authentication.change_password

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.beekeeper.R
import com.example.beekeeper.databinding.FragmentChangePasswordBinding
import com.example.beekeeper.presenter.base_fragment.BaseFragment
import com.example.beekeeper.presenter.event.auth.ChangePasswordEvent
import com.example.beekeeper.presenter.extension.showSnackBar
import com.example.beekeeper.presenter.state.auth.change_password.ChangePasswordState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangePasswordFragment
    :BaseFragment<FragmentChangePasswordBinding>(FragmentChangePasswordBinding::inflate) {

    private val viewModel: ChangePasswordViewModel by viewModels()
    override fun setListeners() {
        setChangeBtnListener()
    }

    private fun setChangeBtnListener(){
        binding.btnChange.setOnClickListener{
            readInputAndTryChange()
        }
    }

    private fun readInputAndTryChange(){
        binding.apply {
            val newPassword = etNewPassword.text.toString()
            val currentPassword = etOldPassword.text.toString()
            val repeatMewPassword = etRepeatNewPassword.text.toString()
            viewModel.onEvent(ChangePasswordEvent.ChangePassword(currentPassword,newPassword,repeatMewPassword))
        }
    }

    override fun bindObservers() {
        bindPasswordChangeStateObserver()
    }

    private fun bindPasswordChangeStateObserver(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.changePasswordState.collect{
                    handleNewState(it)
                }
            }
        }
    }

    private fun handleNewState(state: ChangePasswordState){
        state.errorMessage?.let {
            showErrorMessage(it)
            viewModel.onEvent(ChangePasswordEvent.ResetErrorMessage)
        }

        showOrHideProgressBar(state.isLoading)

        state.passwordChangedSuccessfully?.let {
            passwordChangeSuccessful()
            viewModel.onEvent(ChangePasswordEvent.ResetSuccess)
        }
    }

    private fun passwordChangeSuccessful(){
        binding.root.showSnackBar(getString(R.string.success))
    }

    private fun showOrHideProgressBar(isLoading:Boolean){
        binding.apply {
            progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showErrorMessage(errorMessage:String){
        binding.root.showSnackBar(errorMessage)
        viewModel.onEvent(ChangePasswordEvent.ResetErrorMessage)
    }

}