package com.example.beekeeper.presenter.event.auth

sealed class ChangePasswordEvent {
    data object ResetErrorMessage: ChangePasswordEvent()
    data object ResetSuccess: ChangePasswordEvent()
    data class ChangePassword(val currentPassword:String,val newPassword:String,val repeatNewPassword:String):ChangePasswordEvent()
}