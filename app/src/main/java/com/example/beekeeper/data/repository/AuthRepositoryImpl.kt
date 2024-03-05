package com.example.beekeeper.data.repository

import com.example.beekeeper.data.common.HandleResponse
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.repository.auth.AuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val handleResponse: HandleResponse
) : AuthRepository {
    override suspend fun register(email: String, password: String): Flow<Resource<AuthResult>> {

        return handleResponse.safeApiCall {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        }.map {
            when(it){
                is Resource.Success -> Resource.Success(it.responseData)
                is Resource.Failed -> Resource.Failed(it.message)
                is Resource.Loading -> Resource.Loading()

            }
        }

    }

    override suspend fun login(email: String, password: String): Flow<Resource<AuthResult>> {

        return handleResponse.safeApiCall {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        }
    }

}