package com.example.beekeeper.presenter.screen.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.usecase.credentials.ReadEmailUseCase
import com.example.beekeeper.domain.usecase.credentials.ReadSessionTokenUseCase
import com.example.beekeeper.domain.usecase.user.ReadUserDataUseCase
import com.example.beekeeper.domain.usecase.user.ValidateNameAndLastNameUseCase
import com.example.beekeeper.domain.usecase.user.WriteUserDataUseCase
import com.example.beekeeper.presenter.event.user.ProfilePageEvents
import com.example.beekeeper.presenter.mappers.user.toDomain
import com.example.beekeeper.presenter.mappers.user.toPresentation
import com.example.beekeeper.presenter.model.user.UserCredentials
import com.example.beekeeper.presenter.model.user.UserDataUI
import com.example.beekeeper.presenter.state.user.ProfilePageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val readEmailUseCase: ReadEmailUseCase,
    private val writeUserDataUseCase: WriteUserDataUseCase,
    private val readUserDataUseCase: ReadUserDataUseCase,
    private val readTokenUseCase: ReadSessionTokenUseCase,
    private val validateNameAndLastNameUseCase: ValidateNameAndLastNameUseCase
) : ViewModel() {

    private val _userCredentialsFlow = MutableStateFlow(UserCredentials())
    val userCredentialsFlow: StateFlow<UserCredentials> = _userCredentialsFlow.asStateFlow()


    private val _userDataFlow = MutableStateFlow(ProfilePageState())
    val userDataFlow  = _userDataFlow.asStateFlow()


    fun onEvent(event: ProfilePageEvents) {
        when (event) {
            is ProfilePageEvents.RequestCurrentProfileInfo -> getUserData(event.token)
            ProfilePageEvents.ResetErrorMessageToNull -> updateErrorMessageToNull()
            is ProfilePageEvents.SaveNewProfileInfo -> ifValidThenUpload(event.name,event.lastName)
            ProfilePageEvents.ReadUserCredentials -> readCredentials()
            ProfilePageEvents.UpdateUploadProfileInfoToNull -> updateUploadProfileInfoToNull()
            is ProfilePageEvents.ImageSelected -> _userDataFlow.update {
                Log.d("tag1234", "viewModel imageSelected tryieng to update state")
                it.copy(userDataUI = it.userDataUI?.copy(image = event.image)?:UserDataUI(image = event.image))
            }
        }
    }

    private fun readCredentials() {
        viewModelScope.launch {
            val token = async {
                readTokenUseCase().first() // Wait for the first emission of the flow
            }
            val email = async {
                readEmailUseCase().first()  // Wait for the first emission of the flow
            }

            _userCredentialsFlow.update {
                it.copy(token = token.await(), mail = email.await())
            }
        }
    }

    private fun ifValidThenUpload(name:String, lastName:String){
        if (validateNameAndLastNameUseCase(name,lastName)){
            _userCredentialsFlow.value.token?.let {token->
                writeUserData(
                    userDataUI = UserDataUI(
                        token = token,
                        email = _userCredentialsFlow.value.mail?:"",
                        name = name,
                        lastName = lastName,
                        image = _userDataFlow.value.userDataUI?.image
                    )
                )
            }

        }
    }

    private fun writeUserData(userDataUI: UserDataUI) {
        viewModelScope.launch {
            writeUserDataUseCase.invoke(
                userData = userDataUI.toDomain()
            ).collect { result->
                when (result) {
                    is Resource.Loading -> {
                        _userDataFlow.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        _userDataFlow.update {
                            it.copy(isLoading = false, profileInfoSaved = Unit)
                        }
                    }
                    is Resource.Failed -> {
                        _userDataFlow.update {
                            it.copy(isLoading = false, errorMessage = result.message)
                        }
                    }
                }
            }
        }




    }
     private fun getUserData(token:String) {
        viewModelScope.launch {
            readUserDataUseCase(token).collect {result->
                when (result) {
                    is Resource.Loading -> {
                        _userDataFlow.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        _userDataFlow.update {
                            it.copy(isLoading = false, userDataUI = result.responseData.toPresentation())
                        }
                    }
                    is Resource.Failed -> {
                        _userDataFlow.update {
                            it.copy(isLoading = false, errorMessage = result.message)
                        }
                    }
                }
            }
        }
    }

    private fun updateErrorMessageToNull(){
        _userDataFlow.update {
            it.copy(errorMessage = null)
        }
    }

    private fun updateUploadProfileInfoToNull(){
        _userDataFlow.update {
            it.copy(profileInfoSaved = null)
        }
    }
}