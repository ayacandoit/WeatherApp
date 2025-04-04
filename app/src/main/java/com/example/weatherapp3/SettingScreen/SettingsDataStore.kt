package com.example.weatherapp3.SettingScreen


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings_prefs")

class SettingsDataStore(context: Context) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    companion object {
        val LANGUAGE_KEY = stringPreferencesKey("language")
        val TEMPERATURE_UNIT_KEY = stringPreferencesKey("temperature_unit")
        val WIND_SPEED_UNIT_KEY = stringPreferencesKey("wind_speed_unit")
        val LOCATION_METHOD_KEY = stringPreferencesKey("location_method")
    }

    fun getSetting(key: Preferences.Key<String>, default: String): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[key] ?: default
        }
    }

    suspend fun saveSetting(key: Preferences.Key<String>, value: String) {
        dataStore.edit { it[key] = value }
    }
}