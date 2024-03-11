package com.example.beekeeper.domain.repository.data_store

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun saveString(key: Preferences.Key<String>, input:String)

    fun readString(key: Preferences.Key<String>) : Flow<String>

    suspend fun clearString(key: Preferences.Key<String>)

}