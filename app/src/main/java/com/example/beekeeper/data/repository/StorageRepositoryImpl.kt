package com.example.beekeeper.data.repository

import android.content.ContentResolver
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.repository.storage.StorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.InputStream
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val contentResolver: ContentResolver // Inject ContentResolver to access file content
) : StorageRepository {

    override fun uploadImage(imageStream: InputStream?): Flow<Resource<String>> = flow {
//        try {
//            emit(Resource.Loading())
//            val fileName = System.currentTimeMillis().toString()
//            val storageRef = storage.reference.child("images/$fileName")
//            val uploadTaskSnapshot = imageStream?.let { storageRef.putStream(it).await() }
//                ?: throw Exception("Unable to open file input stream")
//            val downloadUrl = uploadTaskSnapshot.storage.downloadUrl.await().toString()
//
//            emit(Resource.Success(downloadUrl))
//        } catch (e: Exception) {
//            emit(Resource.Failed(e.localizedMessage ?: "An error occurred"))
//        }
    }
}