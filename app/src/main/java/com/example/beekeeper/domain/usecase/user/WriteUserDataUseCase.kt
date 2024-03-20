package com.example.beekeeper.domain.usecase.user

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.user.UserData
import com.example.beekeeper.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WriteUserDataUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(userData: UserData): Flow<Resource<Unit>> =
        userRepository.saveUserData(userData)
}