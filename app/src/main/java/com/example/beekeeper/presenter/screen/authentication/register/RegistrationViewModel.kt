package com.example.beekeeper.presenter.screen.authentication.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.auth.RegisterUseCase
import com.example.beekeeper.presenter.event.RegisterEvent
import com.example.beekeeper.presenter.model.auth.register.UserAuthenticator
import com.example.beekeeper.presenter.state.auth.register.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val registerUseCase: RegisterUseCase): ViewModel() {

    private val _navigationEventFlow = MutableSharedFlow<NavigationEvent>()
    val navigationEventFlow get() = _navigationEventFlow

    private val _registrationState = MutableStateFlow(RegisterState())
    val registrationState = _registrationState.asStateFlow()

    fun onEvent(event: RegisterEvent){
        when(event){
            is RegisterEvent.RegisterUser -> {
                register(event.email,event.password,event.repeatPassword)
            }

            is RegisterEvent.MoveBackToLogin -> {
                giveNavigationFlowNavigationEvent(event.email,event.password)
            }

            is RegisterEvent.ResetErrorMessage -> {
                resetErrorMessageToNull()
            }
        }
    }

    private fun resetErrorMessageToNull(){
        viewModelScope.launch {
            _registrationState.update {
                it.copy(errorMessage = null)
            }
        }

    }


    fun register(email: String, password: String, repeatPassword: String) {

        viewModelScope.launch {
            registerUseCase(email, password, repeatPassword).collect{result->
                when(result){
                    is Resource.Loading -> {
                        _registrationState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        _registrationState.update {
                            it.copy(userAuthenticator = UserAuthenticator(email,password), isLoading = false)
                        }
                    }
                    is Resource.Failed -> {
                        _registrationState.update {
                            it.copy(errorMessage = result.message, isLoading = false)
                        }
                    }
                }
            }
        }
    }

    private fun giveNavigationFlowNavigationEvent(email: String,password: String){
        viewModelScope.launch {
            _navigationEventFlow.emit(NavigationEvent.NavigateBackToLoginPage(email,password))
        }
    }

    sealed interface NavigationEvent{
        data class NavigateBackToLoginPage(val email:String, val password:String):NavigationEvent
    }
}