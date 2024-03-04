package com.example.beekeeper.domain.useCase

import com.example.beekeeper.data.common.Resource
import com.example.beekeeper.domain.repository.AuthRepository
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repository: AuthRepository){

    suspend fun register(email: String, password: String, repeatPassword: String): Flow<Resource<AuthResult>> {
        if (!isValidEmail(email)) {
            return flowOf(Resource.Failed("Invalid email address"))
        }

        if (!isValidPassword(password)) {
            return flowOf(Resource.Failed("Invalid password"))
        }

        if (password != repeatPassword) {
            return flowOf(Resource.Failed("Passwords do not match"))
        }
        return repository.register(email, password).map { resource ->
            when (resource) {
                is Resource.Success -> Resource.Success(resource.responseData)
                is Resource.Failed -> Resource.Failed(resource.message)
                is Resource.Loading -> Resource.Loading()
            }
        }




    }


    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }
}