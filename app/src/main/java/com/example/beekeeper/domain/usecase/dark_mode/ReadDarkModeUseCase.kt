package com.example.beekeeper.domain.usecase.dark_mode

import com.example.beekeeper.domain.repository.data_store.DataStoreRepository
import com.example.beekeeper.domain.utils.PreferencesKeys
import javax.inject.Inject

class ReadDarkModeUseCase @Inject constructor(private val dataStoreRepository: DataStoreRepository) {

    operator fun invoke() = dataStoreRepository.readBoolean(PreferencesKeys.DARK_MODE)

}