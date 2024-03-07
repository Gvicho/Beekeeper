package com.example.beekeeper.data.repository

import android.content.ContentResolver
import android.net.Uri
import android.util.Log.d
import com.example.beekeeper.data.source.remote.internet.model.DamageReportDto
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.damaged_beehives.DamageReport
import com.example.beekeeper.domain.repository.storage.StorageRepository
import com.example.beekeeper.presenter.model.damagedBeehives.DamageReportUI
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage,
    private val database: FirebaseDatabase
) : StorageRepository {

    override fun uploadImage(report: DamageReport): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val downloadUrls: MutableList<String> = mutableListOf()
            for (each in report.imageUris){
                val storageRef = storage.reference.child("images/${each.lastPathSegment}")
                storageRef.putFile(each).await()
                downloadUrls.add(storageRef.downloadUrl.await().toString())
            }
            val readyDamageReport = DamageReportDto(
                id = report.id,
                location = report.location,
                damageDescription = report.damageDescription,
                damageLevelIndicator = report.damageLevelIndicator,
                dateUploaded = report.dateUploaded,
                damageReason = report.damageReason,
                imageUris = downloadUrls
            )

            val reportRef = database.reference.child("damageReports").child(readyDamageReport.id)
            reportRef.setValue(readyDamageReport).await()


            d("DownloadUrls", downloadUrls.toString())
            emit(Resource.Success("downloadUrl"))
        } catch (e: Exception) {
            emit(Resource.Failed(e.localizedMessage ?: "An error occurred"))
        }
    }
}