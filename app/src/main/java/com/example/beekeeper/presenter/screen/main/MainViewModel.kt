package com.example.beekeeper.presenter.screen.main

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.damage_report.GetAllReportsUseCase
import com.example.beekeeper.domain.usecase.dark_mode.ReadDarkModeUseCase
import com.example.beekeeper.domain.usecase.dark_mode.SaveDarkModeUseCase
import com.example.beekeeper.presenter.mappers.toPresentation
import com.example.beekeeper.presenter.model.damaged_beehives.DamageReportUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveDarkModeUseCase: SaveDarkModeUseCase,
    private val readDarkModeUseCase: ReadDarkModeUseCase
) :
    ViewModel() {

        private val _darkModeFlow = MutableSharedFlow<Boolean>()
    val darkModeFlow: SharedFlow<Boolean> get() = _darkModeFlow


    fun writeDarkMode(status: Boolean) {
        viewModelScope.launch {
            saveDarkModeUseCase.invoke(status)
            d("statusBoolean", status.toString())
        }
    }

    fun readDarkMode() {
        viewModelScope.launch {
            readDarkModeUseCase().collect {
                _darkModeFlow.emit(it)
                d("statusBooleanread", it.toString())
            }
        }

    }


}