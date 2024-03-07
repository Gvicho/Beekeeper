package com.example.beekeeper.domain.repository.storage

import android.net.Uri
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.damaged_beehives.DamageReport
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface StorageRepository {

    fun uploadImage(report: DamageReport): Flow<Resource<String>>
}