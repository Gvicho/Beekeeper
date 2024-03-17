package com.example.beekeeper.domain.usecase.assistant

import android.graphics.Bitmap
import android.net.Uri
import com.example.beekeeper.domain.repository.farmer_assistant.AssistantRepository
import javax.inject.Inject

class GetDamageDescUseCase @Inject constructor(private val repository: AssistantRepository) {
    operator fun invoke(images: List<Uri>) = repository.getDescription(images)

}