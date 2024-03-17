package com.example.beekeeper.presenter.screen.themes

import android.util.Log
import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.usecase.dark_mode.ReadDarkModeUseCase
import com.example.beekeeper.domain.usecase.dark_mode.SaveDarkModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ThemesViewModel @Inject constructor(
    private val saveDarkModeUseCase: SaveDarkModeUseCase,
    private val readDarkModeUseCase: ReadDarkModeUseCase
) :
    ViewModel() {


    private val _darkModeFlow = MutableSharedFlow<Boolean>()
    val darkModeFlow: SharedFlow<Boolean> = _darkModeFlow.asSharedFlow()


    fun writeDarkMode(status: Boolean) {
        viewModelScope.launch {
            saveDarkModeUseCase.invoke(status)
            Log.d("statusBoolean", status.toString())
        }
    }

    fun readDarkMode() {
        viewModelScope.launch {
            readDarkModeUseCase().collect {
                d("fasdfsad", it.toString())
                _darkModeFlow.emit(it)

            }
        }

    }

}