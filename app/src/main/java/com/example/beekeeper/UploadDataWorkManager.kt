package com.example.beekeeper

import android.content.Context
import android.util.Log.d
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.beekeeper.domain.model.damaged_beehives.DamageReport
import com.example.beekeeper.domain.model.user.UserData
import com.example.beekeeper.domain.usecase.damage_report.UploadReportUseCase
import com.example.beekeeper.domain.usecase.user.WriteUserDataUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UploadDataWorkManager @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val writeUserDataUseCase: WriteUserDataUseCase,
    private val uploadReportUseCase: UploadReportUseCase


) : CoroutineWorker(appContext = context, params = params) {


    //Inputdatashi unda mivigot cvladi romlis mixedvitac gavsazgvravt romel usecases gamovidzaxebt

    override suspend fun doWork(): Result {
        val uri =  inputData.getString("uri") ?: ""
        writeReport(uri)

        return Result.success()


    }

    private suspend fun writeUserData() {
        val user = UserData(
            email = inputData.getString("email") ?: "",
            token = inputData.getString("token") ?: "",
            name = inputData.getString("name") ?: "",
            lastName = inputData.getString("lastName") ?: "",
            image = inputData.getString("image") ?: ""
        )
        d("WorkerUserData", user.toString())

        writeUserDataUseCase.invoke(user).collect {


        }
    }


    private suspend fun writeReport(uri: String) {

        val report = DamageReport(
            id = 2414,
            damageDescription = "utinfa523sdam",
            damageLevelIndicator = 51,
            dateUploaded = "bds",
            imageUris = listOf(uri.toUri())
        )

        uploadReportUseCase.invoke(report).collect {
            d("dfasf", it.toString())

        }
    }


}