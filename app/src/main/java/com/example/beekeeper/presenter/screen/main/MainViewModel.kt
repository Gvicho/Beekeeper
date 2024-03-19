package com.example.beekeeper.presenter.screen.main

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.usecase.credentials.CancelSessionUseCase
import com.example.beekeeper.domain.usecase.dark_mode.ReadDarkModeUseCase
import com.example.beekeeper.domain.usecase.dark_mode.SaveDarkModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val saveDarkModeUseCase: SaveDarkModeUseCase,
    private val readDarkModeUseCase: ReadDarkModeUseCase,
    private val cancelSessionUseCase: CancelSessionUseCase
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

    fun logOut(){
        viewModelScope.launch {
            cancelSessionUseCase()
        }
    }
}