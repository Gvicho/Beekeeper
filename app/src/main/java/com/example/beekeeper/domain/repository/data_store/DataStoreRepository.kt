package com.example.beekeeper.domain.repository.data_store

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun saveString(key: Preferences.Key<String>, input:String)
    suspend fun saveBoolean(key: Preferences.Key<Boolean>, input:Boolean)

    fun readString(key: Preferences.Key<String>) : Flow<String>

    fun readBoolean(key: Preferences.Key<Boolean>) : Flow<Boolean>

    suspend fun clearString(key: Preferences.Key<String>)
    suspend fun clearBoolean(key: Preferences.Key<Boolean>)

}