package com.example.beekeeper.domain.usecase.auth

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.repository.auth.AuthRepository
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val emailValidator: ValidateEmailUseCase,
    private val repository: AuthRepository)
{
    suspend operator fun invoke(email: String, password: String): Flow<Resource<Boolean>> {
        if(!emailValidator(email)){
            return flowOf(Resource.Failed("Email is Incorrect!"))
        }
        return repository.recoverPassword(email)


    }

}