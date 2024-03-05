package com.example.beekeeper.presenter.event

sealed class LoginEvent {
    data class LoginUserEvent(val email:String,val password:String,val rememberUserChecked:Boolean) :LoginEvent()
    data object MoveUserToRegistrationEvent:LoginEvent()
    data object MoveUserToResetPageEvent:LoginEvent()
    data object ResetErrorStatus:LoginEvent()
}