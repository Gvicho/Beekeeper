package com.example.beekeeper.presenter.screen.user

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.beekeeper.presenter.workers.user_profile.UploadUserDataWorker
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.credentials.ReadEmailUseCase
import com.example.beekeeper.domain.usecase.credentials.ReadSessionTokenUseCase
import com.example.beekeeper.domain.usecase.user.ReadUserDataUseCase
import com.example.beekeeper.domain.usecase.user.ValidateNameAndLastNameUseCase
import com.example.beekeeper.presenter.event.user.ProfilePageEvents
import com.example.beekeeper.presenter.mappers.user.toPresentation
import com.example.beekeeper.presenter.model.user.UserCredentials
import com.example.beekeeper.presenter.model.user.UserDataUI
import com.example.beekeeper.presenter.state.user.ProfilePageState
import com.example.beekeeper.presenter.state.worker_states.WorkerStatusState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val readEmailUseCase: ReadEmailUseCase,
    private val readUserDataUseCase: ReadUserDataUseCase,
    private val readTokenUseCase: ReadSessionTokenUseCase,
    private val validateNameAndLastNameUseCase: ValidateNameAndLastNameUseCase,
    private val application: Application
) : ViewModel() {

    private val _userCredentialsFlow = MutableStateFlow(UserCredentials())
    val userCredentialsFlow: StateFlow<UserCredentials> = _userCredentialsFlow.asStateFlow()


    private val _userDataFlow = MutableStateFlow(ProfilePageState())
    val userDataFlow  = _userDataFlow.asStateFlow()

    private val _workStatus = MutableStateFlow(WorkerStatusState())
    val workStatus = _workStatus.asStateFlow()


    fun onEvent(event: ProfilePageEvents) {
        when (event) {
            is ProfilePageEvents.RequestCurrentProfileInfo -> getUserData(event.token)
            ProfilePageEvents.ResetErrorMessageToNull -> updateErrorMessageToNull()
            is ProfilePageEvents.SaveNewProfileInfo -> ifValidThenUpload(event.name,event.lastName)
            ProfilePageEvents.ReadUserCredentials -> readCredentials()
            is ProfilePageEvents.ImageSelected -> _userDataFlow.update {
                it.copy(userDataUI = it.userDataUI?.copy(image = event.image)?: UserDataUI(image = event.image))
            }
            ProfilePageEvents.ResetBlockToNull -> resetBlocked()
            ProfilePageEvents.ResetCancelToNull -> resetCancelled()
            ProfilePageEvents.ResetFailToNull -> resetFailed()
            ProfilePageEvents.ResetSuccessToNull -> resetUploadedSuccessfully()
        }
    }

    private fun readCredentials() {
        viewModelScope.launch {
            val token = async {
                readTokenUseCase().first() // Wait for the first emission of the flow
            }
            val email = async {
                readEmailUseCase().first()  // Wait for the first emission of the flow
            }

            _userCredentialsFlow.update {
                it.copy(token = token.await(), mail = email.await())
            }
        }
    }

    private fun ifValidThenUpload(name:String, lastName:String){
        if (validateNameAndLastNameUseCase(name,lastName)){
            _userCredentialsFlow.value.token?.let {token->
                writeUserData(
                    userDataUI = UserDataUI(
                        token = token,
                        email = _userCredentialsFlow.value.mail?:"",
                        name = name,
                        lastName = lastName,
                        image = _userDataFlow.value.userDataUI?.image?: ""
                    )
                )
            }

        }
    }

    private fun writeUserData(userDataUI: UserDataUI) {
        val data =
            workDataOf(
                "email" to userDataUI.email,
                "name" to userDataUI.name,
                "lastName" to userDataUI.lastName,
                "image" to userDataUI.image,
                "token" to userDataUI.token
            )

        val worker = OneTimeWorkRequestBuilder<UploadUserDataWorker>()
            .setInputData(data)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(application)
            .enqueueUniqueWork("writeUser", ExistingWorkPolicy.KEEP, worker)

        bindReportUploadWorkObserver()

    }

    private fun bindReportUploadWorkObserver() {
        viewModelScope.launch {
            WorkManager.getInstance(application).getWorkInfosForUniqueWorkFlow("writeUser")
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

     private fun getUserData(token:String) {
        viewModelScope.launch {
            readUserDataUseCase(token).collect {result->
                when (result) {
                    is Resource.Loading -> {
                        _userDataFlow.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        val profile = result.responseData.toPresentation()
                        _userDataFlow.update {
                            it.copy(isLoading = false, userDataUI = profile)
                        }
                    }
                    is Resource.Failed -> {
                        _userDataFlow.update {
                            it.copy(isLoading = false, errorMessage = result.message)
                        }
                    }
                }
            }
        }
    }

    private fun updateErrorMessageToNull(){
        _userDataFlow.update {
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

}