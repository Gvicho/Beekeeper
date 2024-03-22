package com.example.beekeeper

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.beekeeper.domain.usecase.damage_report.UploadReportUseCase
import com.example.beekeeper.domain.usecase.user.WriteUserDataUseCase
import com.example.beekeeper.presenter.workers.reports.UploadReportWorker
import com.example.beekeeper.presenter.workers.user_profile.UploadUserDataWorker
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
    ) : WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return when (workerClassName) {
                UploadUserDataWorker::class.java.name -> UploadUserDataWorker(
                    appContext,
                    workerParameters,
                    writeUserDataUseCase
                )
                UploadReportWorker::class.java.name -> UploadReportWorker(
                    appContext,
                    workerParameters,
                    uploadReportUseCase
                )
                else -> throw IllegalArgumentException("Unknown worker class name: $workerClassName")
            }
        }
    }


    override fun onCreate() {
        super.onCreate()
        createChannels()
    }

    private fun createChannels() {
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



