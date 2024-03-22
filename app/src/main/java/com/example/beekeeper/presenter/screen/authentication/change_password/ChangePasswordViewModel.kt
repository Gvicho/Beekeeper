package com.example.beekeeper.presenter.screen.authentication.change_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.auth.ChangePasswordUseCase
import com.example.beekeeper.domain.usecase.credentials.ReadEmailUseCase
import com.example.beekeeper.presenter.event.auth.ChangePasswordEvent
import com.example.beekeeper.presenter.state.auth.change_password.ChangePasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val  readEmailUseCase: ReadEmailUseCase
) :ViewModel() {


    private val _changePasswordState = MutableStateFlow(ChangePasswordState())
    val changePasswordState = _changePasswordState.asStateFlow()

    private var email:String = ""



    init {
        readEmail()
    }

    fun onEvent(event:ChangePasswordEvent){
        when(event){
            is ChangePasswordEvent.ChangePassword -> changePassword(
                email = email,
                currentPassword = event.currentPassword,
                newPassword = event.newPassword,
                repeatNewPassword = event.repeatNewPassword
            )
            ChangePasswordEvent.ResetErrorMessage -> resetErrorMessageToNull()
            ChangePasswordEvent.ResetSuccess -> resetSuccessToNull()
        }
    }

    private fun changePassword(email:String,currentPassword:String,newPassword:String,repeatNewPassword:String) {
        viewModelScope.launch {
            changePasswordUseCase.invoke(
                email,
                currentPassword,
                newPassword,
                repeatNewPassword
            ).collect{result->
                when(result){
                    is Resource.Failed -> {
                        _changePasswordState.update {
                            it.copy(errorMessage = result.message, isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _changePasswordState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        _changePasswordState.update {
                            it.copy(passwordChangedSuccessfully = result.responseData, isLoading = false)
                        }
                    }
                }
            }
        }
    }

    private fun resetErrorMessageToNull(){
        _changePasswordState.update {
            it.copy(errorMessage = null)
        }
    }

    private fun resetSuccessToNull(){
        _changePasswordState.update {
            it.copy(passwordChangedSuccessfully = null)
        }
    }

    private fun readEmail(){
        viewModelScope.launch {
            readEmailUseCase().collect{
                email = it
            }
        }
    }

}