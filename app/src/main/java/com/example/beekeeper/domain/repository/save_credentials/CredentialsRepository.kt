package com.example.beekeeper.domain.repository.save_credentials

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface CredentialsRepository {

    suspend fun saveToken(key: Preferences.Key<String>, token:String)

    fun readToken(key: Preferences.Key<String>) : Flow<String>

    suspend fun clearToken()

}