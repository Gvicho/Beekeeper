package com.example.beekeeper.domain.usecase.damage_report

import com.example.beekeeper.domain.model.damaged_beehives.DamageReport
import com.example.beekeeper.domain.repository.damage_report.ReportRepository
import javax.inject.Inject

class GetAllReportsUseCase @Inject constructor(private val repository: ReportRepository) {
    suspend operator fun invoke() = repository.getAllDamageReports()

}