package com.example.beekeeper.domain.usecase.credentials

import com.example.beekeeper.domain.repository.data_store.DataStoreRepository
import com.example.beekeeper.domain.utils.PreferencesKeys
import javax.inject.Inject

class SaveTokenUseCase@Inject constructor(private val dataStoreRepository: DataStoreRepository) {

    suspend operator fun invoke(token:String){
        dataStoreRepository.saveString(PreferencesKeys.TOKEN,token)
    }

}