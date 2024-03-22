package com.example.beekeeper.presenter.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.usecase.credentials.ReadSessionTokenUseCase
import com.example.beekeeper.domain.usecase.dark_mode.ReadDarkModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel@Inject constructor(
    private val readSessionTokenUseCase: ReadSessionTokenUseCase,
    private val readDarkModeUseCase: ReadDarkModeUseCase
):ViewModel() {


    private val _darkModeFlow = MutableSharedFlow<Boolean>()
    val darkModeFlow: SharedFlow<Boolean> get() = _darkModeFlow


    private val _navigationEvent = MutableSharedFlow<SplashNavigationEvent>()
    val navigationEvent: SharedFlow<SplashNavigationEvent> get() = _navigationEvent

    fun navigateToNextScreen() {
        viewModelScope.launch {
            val splashDelayInMillis = 1500L
            delay(splashDelayInMillis)
            readSessionTokenUseCase().collect { token ->
                if (token.isEmpty()) {
                    _navigationEvent.emit(SplashNavigationEvent.NavigateToLogin)
                } else {
                    _navigationEvent.emit(SplashNavigationEvent.NavigateToHome)
                }
            }
        }
    }

    sealed interface SplashNavigationEvent{
        data object NavigateToLogin :SplashNavigationEvent
        data object NavigateToHome :SplashNavigationEvent
    }

    fun readDarkMode() {
        viewModelScope.launch {
            readDarkModeUseCase().collect{
                _darkModeFlow.emit(it)
            }
        }
    }
}
