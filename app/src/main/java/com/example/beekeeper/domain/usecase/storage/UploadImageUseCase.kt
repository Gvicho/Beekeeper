package com.example.beekeeper.domain.usecase.storage

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.damaged_beehives.DamageReport
import com.example.beekeeper.domain.repository.storage.StorageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(private val repository: StorageRepository) {

    operator fun invoke(report: DamageReport): Flow<Resource<String>> =
        repository.uploadImage(report)
}