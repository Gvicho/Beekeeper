package com.example.beekeeper.presenter.screen.authentication.reset_password

import com.example.beekeeper.presenter.screen.authentication.register.RegistrationViewModel
import com.example.beekeeper.presenter.state.auth.register.RegisterState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class ResetPasswordViewModel {

    private val _navigationEventFlow = MutableSharedFlow<RegistrationViewModel.NavigationEvent>()
    val navigationEventFlow get() = _navigationEventFlow


    private val _registrationState = MutableStateFlow(RegisterState())
    val registrationState = _registrationState






}