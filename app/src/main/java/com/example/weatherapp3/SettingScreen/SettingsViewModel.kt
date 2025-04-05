package com.example.weatherapp3.SettingScreen


import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsDataStore: SettingsDataStore) : ViewModel() {

    private val _language = MutableStateFlow("English")
    val language: StateFlow<String> = _language

    private val _temperatureUnit = MutableStateFlow("Kelvin")
    val temperatureUnit: StateFlow<String> = _temperatureUnit

    private val _windSpeedUnit = MutableStateFlow("Meter/Sec")
    val windSpeedUnit: StateFlow<String> = _windSpeedUnit

    private val _locationMethod = MutableStateFlow("GPS")
    val locationMethod: StateFlow<String> = _locationMethod


    fun loadSettings() {
        viewModelScope.launch {
            settingsDataStore.getSetting(SettingsDataStore.TEMPERATURE_UNIT_KEY, "Kelvin")
                .collect {
                    _temperatureUnit.value = it
                    println("القيمة الجديدة لدرجة الحرارة: $it")
                }
        }
        viewModelScope.launch {
            settingsDataStore.getSetting(SettingsDataStore.LANGUAGE_KEY, "English")
                .collect { _language.value = it }
        }



        viewModelScope.launch {
            settingsDataStore.getSetting(SettingsDataStore.WIND_SPEED_UNIT_KEY, "Meter/Sec")
                .collect { _windSpeedUnit.value = it }
        }

        viewModelScope.launch {
            settingsDataStore.getSetting(SettingsDataStore.LOCATION_METHOD_KEY, "GPS")
                .collect { _locationMethod.value = it }
        }
    }

    fun saveSetting(key: Preferences.Key<String>, value: String) {
        viewModelScope.launch {
            // احفظ القيمة في DataStore
            settingsDataStore.saveSetting(key, value)

            // حمّل الإعدادات فورًا بعد الحفظ
            loadSettings()

            // (اختياري) اطبع للتأكد من القيمة الجديدة
            println("تم تحديث الإعداد: $key -> $value")
        }
    }

    fun convertTemperature(tempInKelvin: Double): Double {
        return when (temperatureUnit.value) { // استخدام القيمة الحالية مباشرة
            "Celsius" -> tempInKelvin - 273.15
            "Fahrenheit" -> (tempInKelvin - 273.15) * 9 / 5 + 32
            else -> tempInKelvin
        }
    }

    fun convertWindSpeed(speedInMeterPerSec: Double): Double {
        return when (windSpeedUnit.value) { // استخدام القيمة الحالية مباشرة
            "Mile/Hour" -> speedInMeterPerSec * 2.23694
            else -> speedInMeterPerSec
        }
    }
    fun getTemperatureUnit(): String {
        return when (_temperatureUnit.value) { // استخدام القيمة من الـ StateFlow
            "Celsius" -> "°C"
            "Fahrenheit" -> "°F"
            else -> "K"
        }
    }

    fun getWindSpeedUnit(): String {
        return when (_windSpeedUnit.value) {
            "Mile/Hour" -> "mph"
            else -> "m/s"
        }
    }





}

class SettingsFactory(private val settingsDataStore: SettingsDataStore) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(settingsDataStore) as T
    }
}