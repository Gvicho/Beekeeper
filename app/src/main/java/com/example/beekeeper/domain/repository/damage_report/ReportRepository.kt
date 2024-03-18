package com.example.beekeeper.domain.repository.damage_report

import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.damaged_beehives.DamageReport
import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    fun uploadReport(damageReport: DamageReport): Flow<Resource<Unit>>

    fun getAllDamageReports(): Flow<Resource<List<DamageReport>>>
    fun getDamageReportById(reportId: Int): Flow<Resource<DamageReport>>
}