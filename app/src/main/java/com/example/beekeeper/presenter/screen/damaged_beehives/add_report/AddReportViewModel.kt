package com.example.beekeeper.presenter.screen.damaged_beehives.add_report

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.assistant.GetDamageDescUseCase
import com.example.beekeeper.domain.usecase.damage_report.UploadReportUseCase
import com.example.beekeeper.presenter.event.damage_beehives.AddReportPageEvents
import com.example.beekeeper.presenter.mappers.toDomain
import com.example.beekeeper.presenter.model.damaged_beehives.DamageReportUI
import com.example.beekeeper.presenter.state.damage_report.DamageDescriptionState
import com.example.beekeeper.presenter.state.damage_report.DamageReportState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AddReportViewModel @Inject constructor(
    private val uploadReportUseCase: UploadReportUseCase,
    private val getDamageDescUseCase: GetDamageDescUseCase
) : ViewModel() {

    private val _reportUIState = MutableStateFlow(DamageReportState())
    val reportUIState: StateFlow<DamageReportState> = _reportUIState.asStateFlow()

    private val _descFlow = MutableStateFlow(DamageDescriptionState())
    val descFlow: StateFlow<DamageDescriptionState> = _descFlow.asStateFlow()

    fun onEvent(event:AddReportPageEvents){
        when(event){
            is AddReportPageEvents.GetDescription -> getDescription(event.images)
            AddReportPageEvents.ResetErrorMessageOfDescriptionToNull -> resetErrorMessageOfDescriptionStateToNull()
            AddReportPageEvents.ResetErrorMessageOfUploadToNull -> resetErrorMessageOfReportStateToNull()
            is AddReportPageEvents.UploadReport -> uploadReport(desc = event.desc , damageLevel = event.damageLevel, uris = event.uris)
        }
    }

    private fun uploadReport(desc: String, damageLevel: Int, uris: List<Uri>) {
        viewModelScope.launch {
            uploadReportUseCase(
                DamageReportUI(
                    id = 10000 + Random(System.currentTimeMillis()).nextInt(900000),
                    damageDescription = desc,
                    damageLevelIndicator = damageLevel,
                    dateUploaded = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("dd MMMM, yyyy")),
                    imageUris = uris
                ).toDomain()
            ).collect { result ->
                when (result) {
                    is Resource.Failed -> {
                        _reportUIState.update { it.copy(isLoading = false, errorMessage = result.message) }
                    }

                    is Resource.Loading -> {
                        _reportUIState.update { it.copy(isLoading = true) }
                    }

                    is Resource.Success -> {
                        _reportUIState.update { it.copy(isLoading = false, uploadSuccess = Unit) }
                    }
                }
            }
        }
    }

    private fun getDescription(images: List<Uri>) {
        viewModelScope.launch {
            getDamageDescUseCase.invoke(images).collect {result->
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

    private fun resetErrorMessageOfReportStateToNull(){
        _reportUIState.update {
            it.copy(errorMessage = null)
        }
    }

    private fun resetErrorMessageOfDescriptionStateToNull(){
        _descFlow.update {
            it.copy(errorMessage = null)
        }
    }
}