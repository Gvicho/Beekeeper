package com.example.beekeeper.domain.usecase.credentials

import com.example.beekeeper.domain.repository.save_credentials.CredentialsRepository
import javax.inject.Inject

class CancelSessionUseCase@Inject constructor(private val credentialsRepository: CredentialsRepository) {

    suspend operator fun invoke() =credentialsRepository.clearToken()

}