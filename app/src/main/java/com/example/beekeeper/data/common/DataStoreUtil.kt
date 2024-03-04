package com.example.beekeeper.data.common

import android.content.Context
import android.util.Log.d
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.log


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


object DataStoreUtil {
    val REMEMBER = booleanPreferencesKey("remember")
//    val TOKEN = stringPreferencesKey("token")
    val EMAIL = stringPreferencesKey("email")



}