package com.example.beekeeper.presenter.event.auth

sealed class LoginEvent {
    data class LoginUserEvent(val email:String,val password:String,val rememberUserChecked:Boolean) :
        LoginEvent()
    data object MoveUserToRegistrationEvent: LoginEvent()
    data object MoveUserToResetPageEvent: LoginEvent()
    data object ResetErrorStatus: LoginEvent()
}