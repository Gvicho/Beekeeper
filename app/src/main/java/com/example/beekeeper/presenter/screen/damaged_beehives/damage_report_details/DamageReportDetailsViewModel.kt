package com.example.beekeeper.presenter.screen.damaged_beehives.damage_report_details

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.damage_report.GetReportByIdUseCase
import com.example.beekeeper.presenter.mappers.toPresentation
import com.example.beekeeper.presenter.model.damaged_beehives.DamageReportUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DamageReportDetailsViewModel @Inject constructor(private val getReportByIdUseCase: GetReportByIdUseCase) :
    ViewModel() {

    private val _reportFlow = MutableSharedFlow<Resource<DamageReportUI>>()
    val reportFlow: SharedFlow<Resource<DamageReportUI>> = _reportFlow.asSharedFlow()


    fun getReportById(id: Int) {
        viewModelScope.launch {
            getReportByIdUseCase.invoke(id).collect {
                when (it) {
                    is Resource.Loading -> _reportFlow.emit(Resource.Loading())
                    is Resource.Success -> {

                        _reportFlow.emit(Resource.Success(it.responseData.toPresentation()))
                    }

                    is Resource.Failed -> {
                        _reportFlow.emit(Resource.Failed(it.message))
                        Log.d("diagnosis failed", it.message)
                    }

                }
            }
        }
    }


}