package com.example.beekeeper.presenter.screen.damaged_beehives

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.storage.UploadImageUseCase
import com.example.beekeeper.presenter.mappers.toDomain
import com.example.beekeeper.presenter.model.damagedBeehives.DamageReportUI
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class DamagedBeehivesViewModel @Inject constructor(private val uploadImageUseCase: UploadImageUseCase)
    : ViewModel(){

    private val _uploadFlow = MutableSharedFlow<Resource<String>>()
    val uploadFlow: SharedFlow<Resource<String>> = _uploadFlow.asSharedFlow()


    fun uploadImage(uris: List<Uri>) {
        viewModelScope.launch {
            uploadImageUseCase.invoke(    DamageReportUI(
                id = "parturientjj",
                location = "lobortis",
                damageDescription = "splendide",
                damageLevelIndicator = "volumus",
                dateUploaded = 3268,
                damageReason = "mea",
                imageUris = uris
            ).toDomain()).collect {
                when (it) {
                    is Resource.Loading -> _uploadFlow.emit(Resource.Loading())
                    is Resource.Success -> _uploadFlow.emit(Resource.Success(it.responseData))
                    is Resource.Failed -> _uploadFlow.emit(Resource.Failed(it.message))

                }
            }
        }
    }
}