package com.example.beekeeper.domain.usecase.credentials

import com.example.beekeeper.domain.repository.save_credentials.CredentialsRepository
import com.example.beekeeper.domain.utils.PreferencesKeys
import javax.inject.Inject

class ReadSessionTokenUseCase@Inject constructor(private val credentialsRepository: CredentialsRepository) {

    operator fun invoke() = credentialsRepository.readToken(PreferencesKeys.TOKEN)

}