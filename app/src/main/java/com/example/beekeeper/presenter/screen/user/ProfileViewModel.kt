package com.example.beekeeper.presenter.screen.user

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beekeeper.data.repository.UserRepositoryImpl
import com.example.beekeeper.domain.common.Resource
import com.example.beekeeper.domain.model.user.UserData
import com.example.beekeeper.domain.repository.user.UserRepository
import com.example.beekeeper.domain.usecase.credentials.ReadEmailUseCase
import com.example.beekeeper.domain.usecase.credentials.ReadSessionTokenUseCase
import com.example.beekeeper.domain.usecase.user.ReadUserDataUseCase
import com.example.beekeeper.domain.usecase.user.WriteUserDataUseCase
import com.example.beekeeper.presenter.event.user.ProfilePageEvents
import com.example.beekeeper.presenter.mappers.user.toPresentation
import com.example.beekeeper.presenter.model.user.UserDataUI
import com.example.beekeeper.presenter.state.user.ProfilePageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val readEmailUseCase: ReadEmailUseCase,
    private val writeUserDataUseCase: WriteUserDataUseCase,
    private val readUserDataUseCase: ReadUserDataUseCase,
    private val readTokenUseCase: ReadSessionTokenUseCase
) : ViewModel() {

    private val _emailFlow = MutableStateFlow("")
    val emailFlow: StateFlow<String> = _emailFlow.asStateFlow()

    private val _tokenFlow = MutableStateFlow("")
    val tokenFlow: StateFlow<String> = _tokenFlow.asStateFlow()


    private val _userDataFlow = MutableSharedFlow<Resource<UserDataUI>>()
    val userDataFlow: SharedFlow<Resource<UserDataUI>> = _userDataFlow.asSharedFlow()


    fun onEvent(event: ProfilePageEvents) {
        when (event) {
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

    private fun readToken() {
        viewModelScope.launch {
            readTokenUseCase.invoke().collect {
                _tokenFlow.emit(it)
            }
        }
    }

    fun writeUserData(image: String) {
        viewModelScope.launch {
            d("FunctionCalled", "ASDFASDF")
            writeUserDataUseCase.invoke(
                userData = UserData(
                    email = "clarence.riddle@example.com",
                    name = "Gonzalo",
                    lastName = "Jordan",
                    image = image,
                    token = "fdas312fasd211fdsa"
                )
            ).collect {

            }

        }




    }
    //tokeni unda gadaecemodes readUserData funqcias./
     fun getUserData() {
        viewModelScope.launch {
            readUserDataUseCase.invoke("fdas312fasd211fdsa").collect() {
                when (it) {
                    is Resource.Loading -> _userDataFlow.emit(Resource.Loading())
                    is Resource.Success -> {
                        _userDataFlow.emit(
                            Resource.Success(it.responseData.toPresentation())
                        )
                    }
                    is Resource.Failed -> _userDataFlow.emit(
                        Resource.Failed(it.message)
                    )
                }
            }
        }
    }
}