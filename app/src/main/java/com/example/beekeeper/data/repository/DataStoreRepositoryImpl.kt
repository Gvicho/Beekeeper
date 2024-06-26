package com.example.beekeeper.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.beekeeper.domain.repository.data_store.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(private val datastore: DataStore<Preferences>) :
    DataStoreRepository {


    override suspend fun saveString(key: Preferences.Key<String>, input: String) {
        datastore.edit { settings ->
            settings[key] = input
        }
    }

    override suspend fun saveBoolean(key: Preferences.Key<Boolean>, input: Boolean) {
        datastore.edit { settings ->
            settings[key] = input
        }
    }

    override fun readString(key: Preferences.Key<String>): Flow<String> = datastore.data
        .map {
            it[key] ?: ""
        }

    override fun readBoolean(key: Preferences.Key<Boolean>): Flow<Boolean> = datastore.data
        .map {
            it[key] ?: false
        }

    override suspend fun clearString(key: Preferences.Key<String>) {
        datastore.edit { settings ->
            settings.remove(key)
        }
    }

    override suspend fun clearBoolean(key: Preferences.Key<Boolean>) {
        datastore.edit { settings ->
            settings.remove(key)
        }
    }

}