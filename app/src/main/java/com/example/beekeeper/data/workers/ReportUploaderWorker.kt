package com.example.beekeeper.data.workers

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.beekeeper.data.source.remote.internet.model.DamageReportDto
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@HiltWorker
class ReportUploaderWorker@AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted workerParameters: WorkerParameters,
): CoroutineWorker(ctx, workerParameters) {

    init {
        Log.d("tag123", "Work  init")
    }

    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()


    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        val  imageUris= inputData.getStringArray("ReportImages")
        val  damageDescription= inputData.getString("ReportDamageDescription")
        val  id= inputData.getString("ReportId")
        val  dateUploaded= inputData.getString("ReportDateUploaded")
        val damageLevelIndicator = inputData.getInt("ReportDamageLevelIndicator", 0)

        Log.d("tag123", "Work  start info : $damageDescription $id $dateUploaded $damageLevelIndicator")

        if(id == null || damageDescription == null || dateUploaded==null||imageUris==null) return@withContext Result.failure()


        try {
            Log.d("tag123", "Work  start Try ")

            val downloadUrls: MutableList<String> = mutableListOf()  // url list

            for (each in imageUris) {
                val uri = Uri.parse(each)
                val storageRef = storage.reference.child("images/${uri.lastPathSegment}")
                val uploadTask = storageRef.putFile(uri).await()
                val downloadUrl = storageRef.downloadUrl.await().toString()
                downloadUrls.add(downloadUrl)
            }
            Log.d("tag123", "Work  start uploaded images ")

            val readyDamageReport = DamageReportDto(
                id = id,
                damageDescription = damageDescription,
                damageLevelIndicator = damageLevelIndicator,
                dateUploaded = dateUploaded,
                imageUris = imageUris.map { it.toString() }

            )

            val reportRef = database.reference.child("damageReports").child(readyDamageReport.id)
            reportRef.setValue(readyDamageReport).await()


            Log.d("tag123", "Work finished : ${downloadUrls.toString()}")

            return@withContext Result.success()

        } catch (e: Exception) {
            return@withContext Result.failure()
        }
    }
}

