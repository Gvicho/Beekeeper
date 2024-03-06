package com.example.beekeeper.domain.usecase.storage

import android.net.Uri
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.repository.storage.StorageRepository
import kotlinx.coroutines.flow.Flow
import java.io.InputStream
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(private val repository: StorageRepository) {

    suspend operator fun invoke(imageStream: InputStream?): Flow<Resource<String>> =
        repository.uploadImage(imageStream)
}