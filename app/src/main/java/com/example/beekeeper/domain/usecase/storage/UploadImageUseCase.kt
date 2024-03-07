package com.example.beekeeper.domain.usecase.storage

import com.example.beekeeper.domain.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.InputStream
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    //private val repository: StorageRepository
) {

    operator fun invoke(imageStream: InputStream?): Flow<Resource<String>>{
        return flow {

        }
    }

//    operator fun invoke(imageStream: InputStream?): Flow<Resource<String>> =
//        repository.uploadImage(imageStream)
}