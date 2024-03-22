package com.example.beekeeper.presenter.screen.damaged_beehives.add_report

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.beekeeper.presenter.workers.reports.UploadReportWorker
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.assistant.GetDamageDescUseCase
import com.example.beekeeper.presenter.event.damage_beehives.AddReportPageEvents
import com.example.beekeeper.presenter.state.damage_report.DamageDescriptionState
import com.example.beekeeper.presenter.state.worker_states.WorkerStatusState
import com.example.beekeeper.presenter.state.damage_report.imagesList.ImagesListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AddReportViewModel @Inject constructor(
    private val getDamageDescUseCase: GetDamageDescUseCase,
    private val application: Application
) : ViewModel() {


    private val _descFlow = MutableStateFlow(DamageDescriptionState())
    val descFlow: StateFlow<DamageDescriptionState> = _descFlow.asStateFlow()

    private val _imagesList = MutableStateFlow(ImagesListState())
    val imagesList = _imagesList.asStateFlow()

    private val _workStatus = MutableStateFlow(WorkerStatusState())
    val workStatus = _workStatus.asStateFlow()

    fun onEvent(event:AddReportPageEvents){
        when(event){
            AddReportPageEvents.GetDescription -> getDescription()
            AddReportPageEvents.ResetErrorMessageOfDescriptionToNull -> resetErrorMessageOfDescriptionStateToNull()
            is AddReportPageEvents.UploadReport -> uploadReport(desc = event.desc , damageLevel = event.damageLevel)
            is AddReportPageEvents.AddImagesToList -> addImagesToList(event.images)
            is AddReportPageEvents.RemoveImageFromList -> removeImageFromList(event.uri)
            AddReportPageEvents.ResetBlockToNull -> resetBlocked()
            AddReportPageEvents.ResetCancelToNull -> resetCancelled()
            AddReportPageEvents.ResetFailToNull -> resetFailed()
            AddReportPageEvents.ResetSuccessToNull -> resetUploadedSuccessfully()
        }
    }

    private fun addImagesToList(newImages: List<Uri>) {
        _imagesList.update {
            it.copy(images = it.images.plus(newImages))
        }
    }

    private fun removeImageFromList(uri: Uri) {
        _imagesList.update {imagesList->
            imagesList.copy(images = imagesList.images.filter { it != uri })
        }
    }

    private fun uploadReport(desc: String, damageLevel: Int){

        val urisStringArray = _imagesList.value.images.map { it.toString() }.toTypedArray()
        val data =
            workDataOf(
                "id" to generateRandomIdForReport(),
                "damageDescription" to desc,
                "damageLevelIndicator" to damageLevel,
                "dateUploaded" to LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd MMMM, yyyy")),
                "uris" to urisStringArray
            )

        val worker = OneTimeWorkRequestBuilder<UploadReportWorker>()
            .setInputData(data)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(application)
            .enqueueUniqueWork("uploadReport", ExistingWorkPolicy.KEEP, worker)

        bindReportUploadWorkObserver()
    }

    private fun bindReportUploadWorkObserver() {
        viewModelScope.launch {
            WorkManager.getInstance(application).getWorkInfosForUniqueWorkFlow("uploadReport")
                .collect { workInfoList ->
                    workInfoList.forEach { workInfo ->
                        when(workInfo.state){
                            WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING -> {
                                _workStatus.value = WorkerStatusState(isLoading = true)
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                _workStatus.value = WorkerStatusState(
                                    isLoading = false,
                                    uploadedSuccessfully = Unit
                                )
                            }
                            WorkInfo.State.FAILED -> {
                                val errorMessage = workInfo.outputData.getString("error_message") ?: "Unknown error"
                                _workStatus.value = WorkerStatusState(
                                    isLoading = false,
                                    failedMessage = errorMessage
                                )
                            }
                            WorkInfo.State.BLOCKED -> {
                                _workStatus.value = WorkerStatusState(
                                    isLoading = false,
                                    blocked = Unit
                                )
                            }
                            WorkInfo.State.CANCELLED -> {
                                _workStatus.value = WorkerStatusState(
                                    isLoading = false,
                                    wasCanceled = Unit
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun getDescription() {
        viewModelScope.launch {
            getDamageDescUseCase.invoke(_imagesList.value.images).collect {result->
                when (result) {
                    is Resource.Loading -> _descFlow.update { it.copy(isLoading = true) }
                    is Resource.Success -> {
                        _descFlow.update {
                            it.copy(isLoading = false,description = result.responseData)
                        }
                    }
                    is Resource.Failed -> {
                        _descFlow.update {
                            it.copy(isLoading = false,errorMessage = result.message)
                        }
                    }

                }
            }
        }
    }
    private fun resetErrorMessageOfDescriptionStateToNull(){
        _descFlow.update {
            it.copy(errorMessage = null)
        }
    }

    private fun resetUploadedSuccessfully() {
        _workStatus.update { currentState ->
            currentState.copy(uploadedSuccessfully = null)
        }
    }

    private fun resetFailed() {
        _workStatus.update { currentState ->
            currentState.copy(failedMessage = null)
        }
    }

    private fun resetCancelled() {
        _workStatus.update { currentState ->
            currentState.copy(wasCanceled = null)
        }
    }

    private fun resetBlocked() {
        _workStatus.update { currentState ->
            currentState.copy(blocked = null)
        }
    }

    private fun generateRandomIdForReport():Int{
        return 10000 + Random(System.currentTimeMillis()).nextInt(900000)
    }
}