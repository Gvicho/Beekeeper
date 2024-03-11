package com.example.beekeeper.data.repository

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.beekeeper.data.source.remote.internet.mappers.toData
import com.example.beekeeper.data.source.remote.internet.mappers.toDomain
import com.example.beekeeper.data.source.remote.internet.model.DamageReportDto
import com.example.beekeeper.data.workers.ReportUploaderWorker
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.damaged_beehives.DamageReport
import com.example.beekeeper.domain.repository.damage_report.ReportRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val context: Context,
    private val database: FirebaseDatabase
) : ReportRepository {

    override fun uploadReport(damageReport: DamageReport): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())


            val damageReportData = withContext(Dispatchers.Default) {
                damageReport.toData()
            }

            val stringArray: Array<String> = withContext(Dispatchers.Default) {
                damageReportData.imageUris.toTypedArray()
            }

            val inputData = workDataOf(
                "ReportImages" to stringArray,
                "ReportDamageDescription" to damageReportData.damageDescription,
                "ReportId" to damageReportData.id,
                "ReportDateUploaded" to damageReportData.dateUploaded,
                "ReportDamageLevelIndicator" to damageReportData.damageLevelIndicator
            )

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED) // This ensures the device is on an unmetered network, typically Wi-Fi
                .build()

            Log.d("tag123", "middle of upload")

            val uploadWorkRequest = OneTimeWorkRequestBuilder<ReportUploaderWorker>()
                .setInputData(inputData)
                .setConstraints(constraints)
                .addTag("Report")
                .build()

            WorkManager.getInstance(context).enqueue(uploadWorkRequest)

            emit(Resource.Success(Unit))
            Log.d("tag123", "finish of upload")

        }
    }

    override fun getAllDamageReports(): Flow<Resource<List<DamageReport>>> = flow {
        try {
            emit(Resource.Loading())

            val snapshot = database.reference.child("damageReports").get().await()
            val reports = snapshot.children.mapNotNull { it.getValue(DamageReportDto::class.java) }
            Log.d("DamageReports","repository- $reports")
            emit(Resource.Success(reports.map { it.toDomain() }))

        } catch (e: Exception) {
            emit(Resource.Failed(e.message ?: "Failed to fetch damage reports"))
        }

    }.flowOn(Dispatchers.IO)
}
