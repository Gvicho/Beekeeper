package com.example.beekeeper.data.source.remote.internet.mappers.user

import com.example.beekeeper.data.source.remote.internet.model.user.UserDataDto
import com.example.beekeeper.domain.model.user.UserData


fun UserData.toData() = UserDataDto(email = email, userName = userName, imageUrl = imageUrl)

fun UserDataDto.toDomain() = UserData(email = email, userName = userName, imageUrl = imageUrl)