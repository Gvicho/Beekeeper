package com.example.beekeeper.presenter.event.configutrations.themes

sealed class ThemeEvents {
    data object ReadSavedThemeState: ThemeEvents()
    data class SaveThemeState(val isDarkMode:Boolean): ThemeEvents()
}