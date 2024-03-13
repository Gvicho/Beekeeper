package com.example.beekeeper.domain.usecase.analyze_damage

import android.graphics.Bitmap
import com.example.beekeeper.domain.repository.damage_report.ReportRepository
import com.example.beekeeper.domain.repository.evaluator.AnalyzeDamageRepository
import javax.inject.Inject

class GetDamageDescUseCase @Inject constructor(private val repository: AnalyzeDamageRepository) {
    operator fun invoke(images: List<Bitmap>) = repository.getDescription(images)

}