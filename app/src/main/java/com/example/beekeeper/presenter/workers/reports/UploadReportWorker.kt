package com.example.beekeeper.presenter.workers.reports

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.damaged_beehives.DamageReport
import com.example.beekeeper.domain.usecase.damage_report.UploadReportUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UploadReportWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val uploadReportUseCase: UploadReportUseCase
) : CoroutineWorker(appContext = context, params = params) {

    override suspend fun doWork(): Result {
        val uris = inputData.getStringArray("uris")?.map { Uri.parse(it) } ?: listOf()
        val report = DamageReport(
            id = inputData.getInt("id", 0),
            damageDescription = inputData.getString("damageDescription") ?: "",
            damageLevelIndicator = inputData.getInt("damageLevelIndicator", 0),
            dateUploaded = inputData.getString("dateUploaded") ?: "",
            imageUris = uris
        )
        Log.d("UploadUserStatus","in Upload worker start report -> $report")
        return uploadReport(report)
    }

    private suspend fun uploadReport(damageReport: DamageReport):Result {
        var workResult = Result.failure()
        uploadReportUseCase(damageReport).collect {result->
            when(result){
                is Resource.Failed -> {
                    val outputData = workDataOf("error_message" to result.message)
                    workResult = Result.failure(outputData)
                    workResult = Result.success()
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    workResult = Result.success()
                }
            }
        }
        return workResult
    }
}