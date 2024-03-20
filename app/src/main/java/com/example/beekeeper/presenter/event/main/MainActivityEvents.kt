package com.example.beekeeper.presenter.event.main

sealed class MainActivityEvents {
    data object LogOutEvent:MainActivityEvents()
    data object ReadDarkMode:MainActivityEvents()
}