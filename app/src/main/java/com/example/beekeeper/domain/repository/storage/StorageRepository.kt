package com.example.beekeeper.domain.repository.storage

import android.net.Uri
import com.example.beekeeper.domain.common.Resource
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface StorageRepository {

    fun uploadImage(imageStream: InputStream?): Flow<Resource<String>>
}