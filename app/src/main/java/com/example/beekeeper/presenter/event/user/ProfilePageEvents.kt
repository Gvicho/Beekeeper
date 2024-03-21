package com.example.beekeeper.presenter.event.user

import com.example.beekeeper.presenter.model.user.UserDataUI

sealed class ProfilePageEvents {
    data object ResetErrorMessageToNull:ProfilePageEvents()
    data class RequestCurrentProfileInfo(val email:String): ProfilePageEvents()
    data class SaveNewProfileInfo(val userData: UserDataUI):ProfilePageEvents()
    data object ReadUserEmailFromDataStore:ProfilePageEvents()
}