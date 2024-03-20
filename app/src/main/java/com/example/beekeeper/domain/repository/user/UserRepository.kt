package com.example.beekeeper.domain.repository.user

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.user.UserData
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun saveUserData(userData: UserData): Flow<Resource<Unit>>

    fun getUserData(token: String): Flow<Resource<UserData>>
}