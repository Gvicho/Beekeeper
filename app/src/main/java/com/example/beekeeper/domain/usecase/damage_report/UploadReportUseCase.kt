package com.example.beekeeper.domain.usecase.damage_report

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.damaged_beehives.DamageReport
import com.example.beekeeper.domain.repository.damage_report.ReportRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class UploadReportUseCase@Inject constructor(
    private val reportRepository: ReportRepository,
    private val inputUseCase: ValidateInputUseCase
) {
    operator fun invoke(damageReport: DamageReport): Flow<Resource<Unit>> {

        if(inputUseCase(damageReport.damageDescription)){
            return  flowOf(Resource.Failed("Fill all the fields!"))
        }

        return  reportRepository.uploadReport(damageReport)

    }
}