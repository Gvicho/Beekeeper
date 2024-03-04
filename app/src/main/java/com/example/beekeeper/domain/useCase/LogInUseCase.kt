package com.example.beekeeper.domain.useCase

import com.example.beekeeper.data.common.Resource
import com.example.beekeeper.domain.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LogInUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend fun login(email: String, password: String): Flow<Resource<AuthResult>> {

        if (password.isEmpty() || email.isEmpty()) {
            return flowOf(Resource.Failed("Empty Fields!"))
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