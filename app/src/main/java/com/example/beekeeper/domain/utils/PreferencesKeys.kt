package com.example.beekeeper.domain.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val TOKEN = stringPreferencesKey("access_token")
    val MAIL = stringPreferencesKey("access_mail")
    val DARK_MODE = booleanPreferencesKey("dark_mode")
    val Language = booleanPreferencesKey("language")
}