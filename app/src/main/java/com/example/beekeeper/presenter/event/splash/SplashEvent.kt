package com.example.beekeeper.presenter.event.splash

sealed class SplashEvent {
    data object NavigateToNextScreen:SplashEvent()
    data object ReadDarkMode:SplashEvent()
}