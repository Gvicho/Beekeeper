package com.example.beekeeper.presenter.screen.authentication.reset_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.auth.ResetPasswordUseCase
import com.example.beekeeper.presenter.event.ResetPasswordEvent
import com.example.beekeeper.presenter.state.auth.reset_password.ResetPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel(){

    private val _navigationEventFlow = MutableSharedFlow<NavigationEvent>()
    val navigationEventFlow get() = _navigationEventFlow


    private val _resetPasswordPageState = MutableStateFlow(ResetPasswordState())
    val resetPasswordPageState = _resetPasswordPageState.asStateFlow()


    fun onEvent(event: ResetPasswordEvent){
        when(event){
            is ResetPasswordEvent.ResetPassword -> {
                resetPassword(event.email)
            }

            is ResetPasswordEvent.MoveBackToLogin -> {
                giveNavigationFlowNavigationEvent(event.mail)
            }

            is ResetPasswordEvent.ResetErrorMessage -> {
                resetErrorMessageToNull()
            }
        }
    }

    private fun resetPassword(email:String){
        viewModelScope.launch {
            resetPasswordUseCase(email).collect{result->
                when(result){
                    is Resource.Failed -> {
                        _resetPasswordPageState.update {
                            it.copy(errorMessage = result.message, isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _resetPasswordPageState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        onEvent(ResetPasswordEvent.MoveBackToLogin(email))
                    }
                }
            }
        }
    }

    private fun resetErrorMessageToNull(){
        viewModelScope.launch {
            _resetPasswordPageState.update {
                it.copy(errorMessage = null)
            }
        }

    }

    private fun giveNavigationFlowNavigationEvent(email: String){
        viewModelScope.launch {
            _navigationEventFlow.emit(NavigationEvent.NavigateBackToLoginPage(email))
        }
    }

    sealed interface NavigationEvent{
        data class NavigateBackToLoginPage(val email:String):NavigationEvent
    }


}