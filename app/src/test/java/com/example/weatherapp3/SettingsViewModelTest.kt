package com.example.weatherapp3

import androidx.datastore.preferences.core.stringPreferencesKey
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.rules.TestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp3.SettingScreen.SettingsDataStore
import com.example.weatherapp3.SettingScreen.SettingsViewModel
import kotlinx.coroutines.Dispatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.`is`

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()
    private lateinit var settingsDataStore: SettingsDataStore
    private lateinit var viewModel: SettingsViewModel
    private val testDispatcher = StandardTestDispatcher()
    @Before
    fun setup() {
        settingsDataStore = mockk()
        viewModel = SettingsViewModel(settingsDataStore)
        Dispatchers.setMain(testDispatcher)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    @Test
    fun `loadSettings updates state with values from datastore`() = runTest {
        every {
            settingsDataStore.getSetting(
                SettingsDataStore.LANGUAGE_KEY,
                any()
            )
        } returns flowOf("Arabic")
        every {
            settingsDataStore.getSetting(
                SettingsDataStore.TEMPERATURE_UNIT_KEY,
                any()
            )
        } returns flowOf("Celsius")
        every {
            settingsDataStore.getSetting(
                SettingsDataStore.WIND_SPEED_UNIT_KEY,
                any()
            )
        } returns flowOf("Mile/Hour")
        every {
            settingsDataStore.getSetting(
                SettingsDataStore.LOCATION_METHOD_KEY,
                any()
            )
        } returns flowOf("Map")
        viewModel.loadSettings()
        advanceUntilIdle()
        assertThat(viewModel.language.value, `is`("Arabic"))
        assertThat(viewModel.temperatureUnit.value, `is`("Celsius"))
        assertThat(viewModel.windSpeedUnit.value, `is`("Mile/Hour"))
        assertThat(viewModel.locationMethod.value, `is`("Map"))
    }
    @Test
    fun `convertWindSpeed returns correct mph value`() {
        viewModel = SettingsViewModel(settingsDataStore).apply {
            _windSpeedUnit.value = "Mile/Hour"
        }
        val result = viewModel.convertWindSpeed(10.0)
        assertThat(result, `is`(org.hamcrest.number.IsCloseTo.closeTo(22.3694, 0.01)))
    }
    @Test
    fun `convertTemperature returns correct Celsius value`() {
        viewModel = SettingsViewModel(settingsDataStore).apply {
            _temperatureUnit.value = "Celsius"
        }
        val result = viewModel.convertTemperature(300.0)
        assertThat(result, `is`(org.hamcrest.number.IsCloseTo.closeTo(26.85, 0.01)))
    }


}
