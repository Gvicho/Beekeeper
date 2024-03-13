package com.example.beekeeper.presenter.screen.damaged_beehives.report

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.analyze_damage.GetDamageDescUseCase
import com.example.beekeeper.domain.usecase.damage_report.UploadDamageReportUseCase
import com.example.beekeeper.presenter.mappers.toDomain
import com.example.beekeeper.presenter.model.damaged_beehives.DamageReportUI
import com.example.beekeeper.presenter.state.damage_report.DamageReportState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AddReportViewModel @Inject constructor(
    private val uploadDamageReportUseCase: UploadDamageReportUseCase,
    private val getDamageDescUseCase: GetDamageDescUseCase
) : ViewModel() {

    private val _reportUIState = MutableStateFlow(DamageReportState())
    val reportUIState: StateFlow<DamageReportState> = _reportUIState

    private val _descFlow = MutableSharedFlow<Resource<String>>()

    val descFlow: SharedFlow<Resource<String>> = _descFlow.asSharedFlow()


    fun uploadReport(desc: String, damageLevel: Int, uris: List<Uri>) {
        viewModelScope.launch {
            uploadDamageReportUseCase(
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

    fun getDescription(images: List<Bitmap>) {
        Log.d("imagesRepoFail", images.toString())
        viewModelScope.launch {
            getDamageDescUseCase.invoke(images).collect {
                when (it) {
                    is Resource.Loading -> _descFlow.emit(Resource.Loading())
                    is Resource.Success -> {

                        _descFlow.emit(Resource.Success(it.responseData))
                    }
                    is Resource.Failed -> {
                        _descFlow.emit(Resource.Failed(it.message))
                        Log.d("diagnosis failed", it.message)
                    }

                }
            }
        }
    }
}