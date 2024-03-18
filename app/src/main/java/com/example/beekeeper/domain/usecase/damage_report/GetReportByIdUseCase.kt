package com.example.beekeeper.domain.usecase.damage_report

import com.example.beekeeper.domain.repository.damage_report.ReportRepository
import javax.inject.Inject

class GetReportByIdUseCase @Inject constructor(private val repository: ReportRepository) {

    operator fun invoke(id: Int) = repository.getDamageReportById(id)

}