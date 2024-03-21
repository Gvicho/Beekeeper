package com.example.beekeeper.presenter.mappers.user

import com.example.beekeeper.domain.model.user.UserData
import com.example.beekeeper.presenter.model.user.UserDataUI

fun UserDataUI.toDomain() = UserData(
    email = email,
    name = name,
    lastName = lastName,
    image = image,
    token =  token
)


fun UserData.toPresentation() = UserDataUI(
    email = email,
    name = name,
    lastName = lastName,
    image = image,
    token = token
)