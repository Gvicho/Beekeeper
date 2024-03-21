package com.example.beekeeper.presenter.event.user

sealed class ProfilePageEvents {
    data object ResetErrorMessageToNull:ProfilePageEvents()
    data object UpdateUploadProfileInfoToNull:ProfilePageEvents()
    data class RequestCurrentProfileInfo(val token:String): ProfilePageEvents()
    data class ImageSelected(val image:String): ProfilePageEvents()
    data class SaveNewProfileInfo(val name:String,val lastName:String):ProfilePageEvents()
    data object ReadUserCredentials:ProfilePageEvents()
}