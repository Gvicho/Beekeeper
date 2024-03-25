package com.example.beekeeper.presenter.workers.user_profile

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.user.UserData
import com.example.beekeeper.domain.usecase.user.WriteUserDataUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UploadUserDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val writeUserDataUseCase: WriteUserDataUseCase
) : CoroutineWorker(appContext = context, params = params) {

    override suspend fun doWork(): Result {
        val user = UserData(
            email = inputData.getString("email") ?: "",
            token = inputData.getString("token") ?: "",
            name = inputData.getString("name") ?: "",
            lastName = inputData.getString("lastName") ?: "",
            image = inputData.getString("image") ?: ""
        )
        return writeUserData(user)
    }

    private suspend fun writeUserData(userData: UserData):Result {
        var workResult = Result.failure()
        writeUserDataUseCase.invoke(userData).collect {result->
            when(result){
                is Resource.Failed -> {
                    val outputData = workDataOf("error_message" to result.message)
                    workResult = Result.failure(outputData)
                    workResult = Result.success()
                }
                is Resource.Loading -> {
                   // Log.d("UploadUserStatus","in worker, loading")
                }
                is Resource.Success -> {
                    workResult = Result.success()
                }
            }
        }

        return workResult
    }
}