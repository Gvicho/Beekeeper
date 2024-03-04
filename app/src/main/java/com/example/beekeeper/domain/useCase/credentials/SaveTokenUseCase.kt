package com.example.beekeeper.domain.usecase.credentials

import com.example.beekeeper.domain.repository.save_credentials.CredentialsRepository
import com.example.beekeeper.domain.utils.PreferencesKeys
import javax.inject.Inject

class SaveTokenUseCase@Inject constructor(private val credentialsRepository: CredentialsRepository) {

    suspend operator fun invoke(token:String){
        credentialsRepository.saveToken(PreferencesKeys.TOKEN,token)
    }

}