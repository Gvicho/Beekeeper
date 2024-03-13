package com.example.beekeeper.domain.repository.evaluator

import android.graphics.Bitmap
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.damaged_beehives.DamageReport
import kotlinx.coroutines.flow.Flow

interface AnalyzeDamageRepository {


    fun getDescription(images: List<Bitmap>): Flow<Resource<String>>
}