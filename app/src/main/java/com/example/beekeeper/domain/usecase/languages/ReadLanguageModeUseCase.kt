package com.example.beekeeper.domain.usecase.languages

import com.example.beekeeper.domain.repository.data_store.DataStoreRepository
import com.example.beekeeper.domain.utils.PreferencesKeys
import javax.inject.Inject

class ReadLanguageModeUseCase@Inject constructor(private val dataStoreRepository: DataStoreRepository) {

    operator fun invoke() = dataStoreRepository.readBoolean(PreferencesKeys.Language)

}