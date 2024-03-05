package com.example.beekeeper.domain.usecase.auth

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.repository.auth.AuthRepository
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val emailValidator: ValidateEmailUseCase,
    private val passwordValidator: ValidatePasswordUseCase
) {

    suspend operator fun invoke(email: String, password: String): Flow<Resource<AuthResult>> {

        if (password.isEmpty() || email.isEmpty()) {
            return flowOf(Resource.Failed("Empty Fields!"))
        }
        if(!emailValidator(email)){
            return flowOf(Resource.Failed("Email is Incorrect!"))
        }
        if(!passwordValidator(password)){
            return flowOf(Resource.Failed("Password must contain digits, and must be more than 6 characters!"))
        }

        return repository.login(email, password).map { resource ->

            when (resource) {
                is Resource.Success -> Resource.Success(resource.responseData)
                is Resource.Failed -> Resource.Failed(resource.message)
                is Resource.Loading -> Resource.Loading()
            }
        }
    }
}