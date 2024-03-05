package com.example.beekeeper.presenter.event

sealed class RegisterEvent {
    data class RegisterUser(val email: String,val password:String,val repeatPassword:String):RegisterEvent()
    data class MoveBackToLogin(val email: String,val password:String):RegisterEvent()
    data object ResetErrorMessage:RegisterEvent()
}