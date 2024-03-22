package com.example.beekeeper.domain.repository.auth

import com.example.beekeeper.domain.common.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun register(email: String, password: String): Flow<Resource<AuthResult>>
    suspend fun login(email: String, password: String): Flow<Resource<AuthResult>>
    suspend fun recoverPassword(email: String): Flow<Resource<Boolean>>
    suspend fun updatePassword(
        email: String,
        currentPassword: String,
        newPassword: String
    ): Flow<Resource<Unit>>
}