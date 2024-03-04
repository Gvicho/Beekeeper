package com.example.beekeeper.presenter.screen.authentication.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.auth.RegisterUseCase
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RegistrationViewModel @Inject constructor(private val registerUseCase: RegisterUseCase): ViewModel() {

    private val _registerFlow = MutableSharedFlow<Resource<AuthResult>>()
    val registerFlow: SharedFlow<Resource<AuthResult>> = _registerFlow.asSharedFlow()


    fun register(email: String, password: String, repeatPassword: String) {

        viewModelScope.launch {
            registerUseCase(email, password, repeatPassword).collect{
                when(it){
                    is Resource.Loading -> _registerFlow.emit(Resource.Loading())
                    is Resource.Success -> _registerFlow.emit(Resource.Success(it.responseData))
                    is Resource.Failed -> _registerFlow.emit(Resource.Failed(it.message))

                }
            }
        }
    }
}