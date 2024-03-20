package com.example.beekeeper.data.repository

import com.example.beekeeper.data.source.remote.internet.mappers.user.toData
import com.example.beekeeper.data.source.remote.internet.mappers.user.toDomain
import com.example.beekeeper.data.source.remote.internet.model.user.UserDataDto
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.user.UserData
import com.example.beekeeper.domain.repository.user.UserRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val database: FirebaseDatabase) :
    UserRepository {
    override fun saveUserData(userData: UserData): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            val id = userData.userName
            val data = userData.toData()
            database.reference.child("users").child(id).setValue(data).await()

            emit(Resource.Success(Unit))

        } catch (e: Exception) {
            emit(Resource.Failed(e.message ?: "Failed to save user data"))
        }
    }


    override fun getUserData(token: String): Flow<Resource<UserData>> = flow {
        try {
            emit(Resource.Loading())

            val snapshot = database.reference.child("users").child(token).get().await()
            val userDataDto = snapshot.getValue(UserDataDto::class.java)
            if (userDataDto != null) {
                emit(Resource.Success(userDataDto.toDomain()))
            } else {
                emit(Resource.Failed("User data not found"))
            }

        } catch (e: Exception) {
            emit(Resource.Failed(e.message ?: "Failed to fetch user data"))
        }
    }.flowOn(Dispatchers.IO)

    }