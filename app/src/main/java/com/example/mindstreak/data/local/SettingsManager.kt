package com.example.mindstreak.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.settingsDataStore by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {
    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val NOTIFICATIONS_ENABLED_KEY = booleanPreferencesKey("notifications_enabled")
        val IS_DARK_MODE_MANUAL_KEY = booleanPreferencesKey("is_dark_mode_manual")
    }

    val darkModeFlow: Flow<Boolean?> = context.settingsDataStore.data.map { preferences ->
        preferences[DARK_MODE_KEY]
    }

    val notificationsEnabledFlow: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_ENABLED_KEY] ?: false
    }

    val isDarkModeManualFlow: Flow<Boolean> = context.settingsDataStore.data.map { preferences ->
        preferences[IS_DARK_MODE_MANUAL_KEY] ?: false
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
            preferences[IS_DARK_MODE_MANUAL_KEY] = true
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED_KEY] = enabled
        }
    }
}
