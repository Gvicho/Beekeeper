package com.example.beekeeper.presenter.screen.configurations.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.usecase.languages.ReadLanguageModeUseCase
import com.example.beekeeper.domain.usecase.languages.SaveLanguageModeUseCase
import com.example.beekeeper.presenter.event.configutrations.language.LanguageBottomSheetEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguagesViewModel @Inject constructor(
    private val readLanguageModeUseCase: ReadLanguageModeUseCase,
    private val saveLanguageModeUseCase: SaveLanguageModeUseCase
):ViewModel() {

    private val _languageState = MutableStateFlow(false)
    val languageState = _languageState.asStateFlow()

    private val _navigationFlow = MutableSharedFlow<LanguageNavigationEvent>()
    val navigationFlow get() = _navigationFlow.asSharedFlow()


    init {
        readLanguage()
    }

    fun onEvent(event: LanguageBottomSheetEvent){
        when(event){
            is LanguageBottomSheetEvent.SaveLanguageMode -> saveLanguage(event.language)
        }
    }

    private fun saveLanguage(language:Boolean){
        viewModelScope.launch {
            saveLanguageModeUseCase(language)
        }
        makeNavigationClose()
    }

    private fun makeNavigationClose(){
        viewModelScope.launch{
            _navigationFlow.emit(LanguageNavigationEvent.CloseBottomSheet)
        }
    }

    private fun readLanguage(){

        viewModelScope.launch {
            readLanguageModeUseCase().collect{language->
                _languageState.update {
                    language
                }
            }

        }

    }


    sealed interface LanguageNavigationEvent{
        data object CloseBottomSheet:LanguageNavigationEvent
    }
}