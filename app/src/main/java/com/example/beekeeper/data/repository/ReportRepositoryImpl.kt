package com.example.beekeeper.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Log.d
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.beekeeper.data.source.remote.internet.mappers.toData
import com.example.beekeeper.data.source.remote.internet.mappers.toDomain
import com.example.beekeeper.data.source.remote.internet.model.damaged_beehives.DamageReportDto
import com.example.beekeeper.data.workers.ReportUploaderWorker
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.damaged_beehives.DamageReport
import com.example.beekeeper.domain.repository.damage_report.ReportRepository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val context: Context,
    private val database: FirebaseDatabase,
    private val storage: FirebaseStorage
) : ReportRepository {

    override fun uploadReport(damageReport: DamageReport): Flow<Resource<Unit>> = flow {

        try {

            emit(Resource.Loading())
            val reportDto = damageReport.toData()
            val id = reportDto.id
            val imageUrls = damageReport.imageUris.map {uri->
                uploadImageAndGetUrl(uri)
            }
           reportDto.imageUris = imageUrls
            database.reference.child("reports").child(id.toString()).setValue(reportDto).await()
            emit(Resource.Success(Unit))

        }catch (e: Exception){
            d("errorUploadingReportsToDatabase", e.toString())
            emit(Resource.Failed("Failed to upload"))
        }

    }


    override fun getAllDamageReports(): Flow<Resource<List<DamageReport>>> = flow {
        try {
            emit(Resource.Loading())

            val snapshot = database.reference.child("damageReports").get().await()
            val reports = snapshot.children.mapNotNull { it.getValue(DamageReportDto::class.java) }
            Log.d("DamageReports", "repository- $reports")
            emit(Resource.Success(reports.map {
                it.toDomain()
            }))

        } catch (e: Exception) {
            emit(Resource.Failed(e.message ?: "Failed to fetch damage reports"))
        }

    }.flowOn(Dispatchers.IO)


    override fun getDamageReportById(reportId: Int): Flow<Resource<DamageReport>> = flow {
        try {
            emit(Resource.Loading())

            val snapshot =
                database.reference.child("damageReports").child(reportId.toString()).get().await()

            val reportDto = snapshot.getValue(DamageReportDto::class.java)
            val report = reportDto?.toDomain()

            if (report != null) {
                emit(Resource.Success(report))
            } else {
                emit(Resource.Failed("Damage report not found"))
            }

        } catch (e: Exception) {
            emit(Resource.Failed(e.message ?: "Failed to fetch damage report"))
        }
    }.flowOn(Dispatchers.IO)


    private suspend fun uploadImageAndGetUrl(imageUri: Uri): String = withContext(Dispatchers.IO) {
        val storageRef = storage.reference.child("images/${imageUri.lastPathSegment}")
        val uploadTask = storageRef.putFile(imageUri).await()
        val downloadUrl = uploadTask.storage.downloadUrl.await()
        downloadUrl.toString()
    }
}
