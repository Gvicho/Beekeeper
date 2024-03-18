package com.example.beekeeper.domain.usecase.assistant

import android.graphics.Bitmap
import android.net.Uri
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.repository.farmer_assistant.AssistantRepository
import com.example.beekeeper.domain.usecase.damage_report.ValidateInputUseCase
import com.example.beekeeper.domain.usecase.damage_report.ValidateUrisUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetDamageDescUseCase @Inject constructor(
    private val validateUrisUseCase: ValidateUrisUseCase,
    private val repository: AssistantRepository
) {
    operator fun invoke(images: List<Uri>): Flow<Resource<String>>{

        if (validateUrisUseCase(images)){
            return  flowOf(Resource.Failed("Pick images!"))
        }
        return  repository.getDescription(images)
    }
}