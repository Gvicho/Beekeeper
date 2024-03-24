package com.example.beekeeper.domain.usecase.languages

import com.example.beekeeper.domain.repository.data_store.DataStoreRepository
import com.example.beekeeper.domain.utils.PreferencesKeys
import javax.inject.Inject

class SaveLanguageModeUseCase @Inject constructor(private val dataStoreRepository: DataStoreRepository) {
    suspend operator fun invoke( status : Boolean){
        dataStoreRepository.saveBoolean(PreferencesKeys.Language,status)
    }
}