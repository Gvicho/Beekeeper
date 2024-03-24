package com.example.beekeeper.presenter.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.credentials.CancelSessionUseCase
import com.example.beekeeper.domain.usecase.credentials.ReadSessionTokenUseCase
import com.example.beekeeper.domain.usecase.dark_mode.ReadDarkModeUseCase
import com.example.beekeeper.domain.usecase.languages.ReadLanguageModeUseCase
import com.example.beekeeper.domain.usecase.user.ReadUserDataUseCase
import com.example.beekeeper.presenter.event.main.MainActivityEvents
import com.example.beekeeper.presenter.mappers.user.toPresentation
import com.example.beekeeper.presenter.model.user.UserDataUI
import com.example.beekeeper.presenter.state.configurations.AppConfigurationsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val readDarkModeUseCase: ReadDarkModeUseCase,
    private val cancelSessionUseCase: CancelSessionUseCase,
    private val readSessionTokenUseCase: ReadSessionTokenUseCase,
    private val readUserDataUseCase: ReadUserDataUseCase,
    private val readLanguageModeUseCase: ReadLanguageModeUseCase
) : ViewModel() {

    private val _configurationsState = MutableStateFlow(AppConfigurationsState())
    val configurationsState = _configurationsState.asStateFlow()


    private val _pageNavigationEvent = MutableStateFlow<MessagePageNavigationEvents?>(null)
    val pageNavigationEvent get() = _pageNavigationEvent.asStateFlow()


    private val _userProfileState = MutableStateFlow(UserDataUI())
    val userProfileState = _userProfileState.asStateFlow()



    private var sessionToken:String = ""

    init {
        readToken()
    }


    fun onEvent(event:MainActivityEvents){
        when(event){
            MainActivityEvents.LogOutEvent -> logOut()
            MainActivityEvents.ReadDarkMode -> readDarkMode()
            is MainActivityEvents.IntentReceivedWithReportId -> ifSessionEmitNavigateEvent(event.reportId)
            MainActivityEvents.ResetNavigationToNull -> resetNavigationToNull()
            MainActivityEvents.ReadLanguageConfiguration -> readLanguageOption()
        }
    }

    private fun readDarkMode() {
        viewModelScope.launch {
            readDarkModeUseCase().collect {theme->
                _configurationsState.update {
                    it.copy(theme = theme)
                }
            }
        }
    }

    private fun readLanguageOption(){
        viewModelScope.launch {
            readLanguageModeUseCase().collect {language->
                _configurationsState.update {
                    it.copy(languages = language)
                }
            }
        }
    }

    private fun logOut(){
        viewModelScope.launch {
            cancelSessionUseCase()
        }
    }

    private fun ifSessionEmitNavigateEvent(reportId:Int){
        if(sessionToken.isNotEmpty()){
            navigationEventToSavedAnalytic(reportId)
        }
    }

    private fun navigationEventToSavedAnalytic(reportId:Int){
        viewModelScope.launch {
            _pageNavigationEvent.emit(MessagePageNavigationEvents.NavigateToReportDetailsPage(reportId))
        }
    }

    private fun readUserData(token:String){
        viewModelScope.launch {
            readUserDataUseCase(token).collect{result->
                when(result){
                    is Resource.Failed -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        _userProfileState.update {
                            result.responseData.toPresentation()
                        }
                    }
                }
            }
        }
    }

    private fun readToken(){
        viewModelScope.launch {
            readSessionTokenUseCase().collect { token ->
                sessionToken = token
                readUserData(token)
            }
        }
    }

    private fun resetNavigationToNull(){
        _pageNavigationEvent.update {
            null
        }
    }

    sealed interface MessagePageNavigationEvents{
        data class NavigateToReportDetailsPage(val reportId:Int): MessagePageNavigationEvents
    }
}