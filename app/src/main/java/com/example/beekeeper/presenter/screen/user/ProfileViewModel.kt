package com.example.beekeeper.presenter.screen.user

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.usecase.credentials.ReadEmailUseCase
import com.example.beekeeper.domain.usecase.user.ReadUserDataUseCase
import com.example.beekeeper.domain.usecase.user.WriteUserDataUseCase
import com.example.beekeeper.presenter.event.user.ProfilePageEvents
import com.example.beekeeper.presenter.state.user.ProfilePageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val readEmailUseCase: ReadEmailUseCase,
    private val writeUserDataUseCase: WriteUserDataUseCase,
    private val readUserDataUseCase: ReadUserDataUseCase
    ) : ViewModel() {

    private val _emailFlow = MutableStateFlow("")
    val emailFlow: StateFlow<String>  = _emailFlow.asStateFlow()


    private val _profileInfoFlow = MutableStateFlow(ProfilePageState())
    val profileInfoFlow: StateFlow<ProfilePageState>  = _profileInfoFlow.asStateFlow()

    fun onEvent(event:ProfilePageEvents){
        when(event){
            ProfilePageEvents.ReadUserEmailFromDataStore -> readEmail()
            is ProfilePageEvents.RequestCurrentProfileInfo -> TODO()
            ProfilePageEvents.ResetErrorMessageToNull -> TODO()
            is ProfilePageEvents.SaveNewProfileInfo -> TODO()
        }
    }

     private fun readEmail() {
        viewModelScope.launch {
            readEmailUseCase.invoke().collect {
                d("viewmodelEmail", it)
                _emailFlow.emit(it)
            }
        }
    }
}