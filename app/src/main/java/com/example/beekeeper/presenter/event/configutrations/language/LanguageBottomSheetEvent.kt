package com.example.beekeeper.presenter.event.configutrations.language

sealed class LanguageBottomSheetEvent {
    data class SaveLanguageMode(val language:Boolean):LanguageBottomSheetEvent()
}