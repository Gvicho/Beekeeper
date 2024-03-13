package com.example.beekeeper.domain.usecase.dark_mode

import com.example.beekeeper.domain.repository.data_store.DataStoreRepository
import com.example.beekeeper.domain.utils.PreferencesKeys
import javax.inject.Inject

class SaveDarkModeUseCase @Inject constructor(private val dataStoreRepository: DataStoreRepository) {

    suspend operator fun invoke( status : Boolean){
        dataStoreRepository.saveBoolean(PreferencesKeys.DARK_MODE,status)
    }

}