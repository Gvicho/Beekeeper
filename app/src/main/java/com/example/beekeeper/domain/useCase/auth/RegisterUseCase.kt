package com.example.beekeeper.domain.usecase.auth

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.repository.auth.AuthRepository
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val emailValidator: ValidateEmailUseCase,
    private val passwordValidator: ValidatePasswordUseCase
){

    suspend operator fun invoke(email: String, password: String, repeatPassword: String): Flow<Resource<AuthResult>> {

        if (!emailValidator(email)) {
            return flowOf(Resource.Failed("Invalid email address!"))
        }

        if (password != repeatPassword) {
            return flowOf(Resource.Failed("Passwords do not match!"))
        }

        if (!passwordValidator(password)) {
            return flowOf(Resource.Failed("Invalid password!"))
        }

        return repository.register(email, password)

    }
}