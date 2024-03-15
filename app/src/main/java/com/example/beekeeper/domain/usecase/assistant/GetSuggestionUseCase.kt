package com.example.beekeeper.domain.usecase.assistant

import android.graphics.Bitmap
import com.example.beekeeper.domain.repository.farmer_assistant.AssistantRepository
import javax.inject.Inject

class GetSuggestionUseCase @Inject constructor(private val repository: AssistantRepository) {
    operator fun invoke() = repository.getSuggestions()

}