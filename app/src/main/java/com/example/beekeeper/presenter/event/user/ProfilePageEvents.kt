package com.example.beekeeper.presenter.event.user

sealed class ProfilePageEvents {
    data object ResetErrorMessageToNull:ProfilePageEvents()
    data class RequestCurrentProfileInfo(val token:String): ProfilePageEvents()
    data class ImageSelected(val image:String): ProfilePageEvents()
    data class SaveNewProfileInfo(val name:String,val lastName:String):ProfilePageEvents()
    data object ReadUserCredentials:ProfilePageEvents()
    data object ResetBlockToNull: ProfilePageEvents()
    data object ResetFailToNull: ProfilePageEvents()
    data object ResetSuccessToNull: ProfilePageEvents()
    data object ResetCancelToNull: ProfilePageEvents()
}