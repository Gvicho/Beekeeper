package com.example.beekeeper.presenter.screen.authentication.change_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.usecase.auth.ChangePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(private val changePasswordUseCase: ChangePasswordUseCase) :
    ViewModel() {


    fun changePassword() {
        viewModelScope.launch {
            changePasswordUseCase.invoke(
                "kotejaparidze9@gmail.com",
                "Kotejaparidze20**",
                "kotekote20*",
                "kotekote20*"
            ).collect{

            }
        }
    }

}