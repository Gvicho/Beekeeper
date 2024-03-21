package com.example.beekeeper

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.beekeeper.domain.usecase.damage_report.UploadReportUseCase
import com.example.beekeeper.domain.usecase.user.WriteUserDataUseCase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BeekeepersApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: CustomWorkerFactory
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()


    class CustomWorkerFactory @Inject constructor(
        private val writeUserDataUseCase: WriteUserDataUseCase,
        private val uploadReportUseCase: UploadReportUseCase
    ) :
        WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker = UploadDataWorkManager(
            appContext,
            workerParameters,
            writeUserDataUseCase,
            uploadReportUseCase
        )
    }


    override fun onCreate() {
        super.onCreate()
        createChannels()
    }

    private fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_analytics",
                "analytics",
                NotificationManager.IMPORTANCE_HIGH
            )

            val manager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}



