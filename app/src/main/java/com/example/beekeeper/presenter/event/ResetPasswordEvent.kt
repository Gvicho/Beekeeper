package com.example.beekeeper.presenter.event

sealed class ResetPasswordEvent{
    data class ResetPassword(val email: String):ResetPasswordEvent()
    data class MoveBackToLogin(val mail:String):ResetPasswordEvent()
    data object ResetErrorMessage:ResetPasswordEvent()
}