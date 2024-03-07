package com.example.beekeeper.data.repository

import android.util.Log.d
import com.example.beekeeper.data.common.HandleResponse
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.repository.auth.AuthRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val handleResponse: HandleResponse
) : AuthRepository {
    override suspend fun register(email: String, password: String): Flow<Resource<AuthResult>> {

        return handleResponse.safeApiCall {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        }

    }

    override suspend fun login(email: String, password: String): Flow<Resource<AuthResult>> {

        return handleResponse.safeApiCall {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        }
    }

    override suspend fun recoverPassword(email: String): Flow<Resource<Boolean>> =
        flow{
            try {
                emit(Resource.Loading())
                firebaseAuth.sendPasswordResetEmail(email).await()  // if there is error it will throw exception
                emit(Resource.Success(true))
            }catch (e:Exception){
                emit(Resource.Failed(e.message ?: "Failed with Exception!"))
                d("exceptionInRepo", e.toString())
            }

        }  // this doesn't validate if email is valid and there is any account by that email (FireBase Security issues)




}