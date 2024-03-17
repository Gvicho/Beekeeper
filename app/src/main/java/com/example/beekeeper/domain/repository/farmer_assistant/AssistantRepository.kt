package com.example.beekeeper.domain.repository.farmer_assistant

import android.graphics.Bitmap
import android.net.Uri
import com.example.beekeeper.domain.common.Resource
import kotlinx.coroutines.flow.Flow

interface AssistantRepository {


    fun getDescription(imagesUris: List<Uri>): Flow<Resource<String>>
    fun getSuggestions(): Flow<Resource<String>>
}