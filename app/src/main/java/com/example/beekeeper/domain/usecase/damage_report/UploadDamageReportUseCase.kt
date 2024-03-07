package com.example.beekeeper.domain.usecase.damage_report

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.damaged_beehives.DamageReport
import com.example.beekeeper.domain.repository.damage_report.ReportRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UploadDamageReportUseCase@Inject constructor(
    private val reportRepository: ReportRepository
) {
    operator fun invoke(damageReport: DamageReport): Flow<Resource<Unit>> = reportRepository.uploadReport(damageReport)
}