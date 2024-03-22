package com.example.beekeeper.domain.usecase.auth

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.repository.auth.AuthRepository
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val passwordValidator: ValidatePasswordUseCase

    ) {

    suspend operator fun invoke(
        email: String,
        currentPassword: String,
        newPassword: String,
        repeatNewPassword: String
    ): Flow<Resource<Unit>> {
        if(!passwordValidator(newPassword)){
            return flowOf(Resource.Failed("Invalid password!"))
        }

        if(newPassword != repeatNewPassword){
            return flowOf(Resource.Failed("Passwords don't match"))
        }

        return repository.updatePassword(
            email = email,
            currentPassword = currentPassword,
            newPassword = newPassword)

    }
}