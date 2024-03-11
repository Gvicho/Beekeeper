package com.example.beekeeper.domain.usecase.credentials

import com.example.beekeeper.domain.repository.data_store.DataStoreRepository
import com.example.beekeeper.domain.utils.PreferencesKeys
import javax.inject.Inject

class ReadSessionTokenUseCase@Inject constructor(private val dataStoreRepository: DataStoreRepository) {

    operator fun invoke() = dataStoreRepository.readString(PreferencesKeys.TOKEN)

}