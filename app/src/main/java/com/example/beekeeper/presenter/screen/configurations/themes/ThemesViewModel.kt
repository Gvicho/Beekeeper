package com.example.beekeeper.presenter.screen.configurations.themes

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.usecase.dark_mode.ReadDarkModeUseCase
import com.example.beekeeper.domain.usecase.dark_mode.SaveDarkModeUseCase
import com.example.beekeeper.presenter.event.configutrations.themes.ThemeEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ThemesViewModel @Inject constructor(
    private val saveDarkModeUseCase: SaveDarkModeUseCase,
    private val readDarkModeUseCase: ReadDarkModeUseCase
) : ViewModel() {


    private val _darkModeFlow = MutableStateFlow(false)
    val darkModeFlow: StateFlow<Boolean> = _darkModeFlow.asStateFlow()

    fun onEvent(event: ThemeEvents){
        when(event){
            ThemeEvents.ReadSavedThemeState -> readDarkMode()
            is ThemeEvents.SaveThemeState -> writeDarkMode(event.isDarkMode)
        }
    }

    private fun writeDarkMode(status: Boolean) {
        viewModelScope.launch {
            saveDarkModeUseCase.invoke(status)
            d("statusBoolean", status.toString())
        }
    }

    private fun readDarkMode() {
        viewModelScope.launch {
            readDarkModeUseCase().collect {mode->
                d("tag1234", "$mode in viewModel ")
                _darkModeFlow.update {
                    mode
                }
            }
        }
    }

}