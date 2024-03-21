package com.example.beekeeper.data.source.remote.internet.mappers.user

import com.example.beekeeper.data.source.remote.internet.model.user.UserDataDto
import com.example.beekeeper.domain.model.user.UserData


fun UserData.toData() = UserDataDto(
    email = email,
    name = name,
    lastName = lastName,
    image = image,
    token = token
)

fun UserDataDto.toDomain() = UserData(
    email = email,
    name = name,
    lastName = lastName,
    image = image,
    token = token
)