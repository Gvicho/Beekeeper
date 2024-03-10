package com.example.beekeeper.presenter.screen.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.auth.LogInUseCase
import com.example.beekeeper.domain.usecase.credentials.SaveTokenUseCase
import com.example.beekeeper.presenter.event.LoginEvent
import com.example.beekeeper.presenter.state.auth.login.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val loginUseCase: LogInUseCase,
    private val saveTokenUseCase: SaveTokenUseCase
) : ViewModel() {

    private val _loginUIState =  MutableStateFlow(LoginUiState())
    val loginUIState : StateFlow<LoginUiState> = _loginUIState

    private val _loginPageNavigationEvent = MutableSharedFlow<LoginNavigationEvent>()
    val loginPageNavigationEvent get() = _loginPageNavigationEvent.asSharedFlow()


    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.LoginUserEvent -> {
                logIn(event.email,event.password,event.rememberUserChecked)
            }
            LoginEvent.MoveUserToRegistrationEvent -> {
                setNavigationEventFlowToRegister()
            }
            LoginEvent.ResetErrorStatus -> {
                updateErrorMessage(null)
            }

            LoginEvent.MoveUserToResetPageEvent -> setNavigationEventFlowToPasswordReset()
        }
    }


    private fun logIn(email: String, password: String, rememberMe:Boolean) {

        viewModelScope.launch {
            loginUseCase(email, password).collect {result->
                when (result) {
                    is Resource.Loading -> {
                        _loginUIState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        val token = result.responseData.user?.uid ?: ""
                        _loginUIState.update {
                            it.copy(accessToken =token)
                        }
                        if(rememberMe)saveTokenInDataStoreIfSelectedSettings(token)
                        setNavigationEventFlowToHome()
                    }
                    is Resource.Failed -> {
                        _loginUIState.update {
                            it.copy(errorMessage = result.message, isLoading = false)
                        }
                    }
                }

            }
        }
    }

    private fun saveTokenInDataStoreIfSelectedSettings(token:String){
        viewModelScope.launch {
            saveTokenUseCase(token)
        }
    }

    private fun setNavigationEventFlowToHome(){
        viewModelScope.launch {
            _loginPageNavigationEvent.emit(LoginNavigationEvent.NavigateToHomePageEvent)
        }
    }

    private fun setNavigationEventFlowToRegister(){
        viewModelScope.launch {
            _loginPageNavigationEvent.emit(LoginNavigationEvent.NavigateToRegistrationEvent)
        }
    }

    private fun setNavigationEventFlowToPasswordReset(){
        viewModelScope.launch {
            _loginPageNavigationEvent.emit(LoginNavigationEvent.NavigateToResetPasswordPageEvent)
        }
    }

    private fun updateErrorMessage(message:String?){
        _loginUIState.update {
            it.copy(errorMessage = message)
        }
    }


    sealed interface LoginNavigationEvent{
        data object NavigateToHomePageEvent:LoginNavigationEvent
        data object NavigateToRegistrationEvent:LoginNavigationEvent
        data object NavigateToResetPasswordPageEvent:LoginNavigationEvent
    }

}

