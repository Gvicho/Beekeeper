package com.example.beekeeper.presenter.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.usecase.credentials.CancelSessionUseCase
import com.example.beekeeper.domain.usecase.dark_mode.ReadDarkModeUseCase
import com.example.beekeeper.presenter.event.main.MainActivityEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val readDarkModeUseCase: ReadDarkModeUseCase,
    private val cancelSessionUseCase: CancelSessionUseCase
) : ViewModel() {


    private val _darkModeFlow = MutableSharedFlow<Boolean>()
    val darkModeFlow: SharedFlow<Boolean> get() = _darkModeFlow

    fun onEvent(event:MainActivityEvents){
        when(event){
            MainActivityEvents.LogOutEvent -> logOut()
            MainActivityEvents.ReadDarkMode -> readDarkMode()
        }
    }

    private fun readDarkMode() {
        viewModelScope.launch {
            readDarkModeUseCase().collect {
                _darkModeFlow.emit(it)
            }
        }

    }

    private fun logOut(){
        viewModelScope.launch {
            cancelSessionUseCase()
        }
    }
}