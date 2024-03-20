package com.example.beekeeper.domain.usecase.user

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.user.UserData
import com.example.beekeeper.domain.repository.user.UserRepository
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadUserDataUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(token: String): Flow<Resource<UserData>> =
        userRepository.getUserData(token)
}