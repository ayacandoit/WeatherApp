package com.example.forecastify.settings

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.weatherapp3.R
import com.example.weatherapp3.SettingScreen.SettingsDataStore
import java.text.NumberFormat
import java.util.Locale
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.example.weatherapp3.SettingScreen.SettingsViewModel


@Composable
fun SettingsScreen(navHostController: NavHostController, viewModel: SettingsViewModel) {
    val language by viewModel.language.collectAsState()
    val temperatureUnit by viewModel.temperatureUnit.collectAsState()
    val windSpeedUnit by viewModel.windSpeedUnit.collectAsState()
    val locationMethod by viewModel.locationMethod.collectAsState()

    viewModel.loadSettings()

    val context = LocalContext.current

    val settingsOptions = listOf(
        "Language" to listOf(
            stringResource(R.string.english),
            stringResource(R.string.arabic),
            stringResource(R.string.defaultt)
        ),
        "Temperature Unit" to listOf(
            stringResource(R.string.kelvin_k),
            stringResource(R.string.celsius_c),
            stringResource(R.string.fahrenheit_f)
        ),
        "Wind Speed Unit" to listOf(
            stringResource(R.string.meter_sec),
            stringResource(R.string.mile_hour)
        ),
        "Location Method" to listOf(stringResource(R.string.gps), stringResource(R.string.map))
    )

    val selectedOptions = remember { mutableStateMapOf<String, String>() }

    selectedOptions["Language"] = language
    selectedOptions["Temperature Unit"] = temperatureUnit
    selectedOptions["Wind Speed Unit"] = windSpeedUnit
    selectedOptions["Location Method"] = locationMethod

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(settingsOptions) { (title, options) ->
            SettingSection(title, options, selectedOptions) { key, value ->
                when (key) {
                    "Language" -> {
                        viewModel.saveSetting(SettingsDataStore.LANGUAGE_KEY, value)
                        if (value == "Arabic" || value == "العربية") {
                            LanguageConverter.changeLanguage(context, "ar")
                        }
                        if (value == "English" || value == "الإنجليزية") {
                            LanguageConverter.changeLanguage(context, "en")
                        }
                    }

                    "Temperature Unit" -> viewModel.saveSetting(
                        SettingsDataStore.TEMPERATURE_UNIT_KEY,
                        value
                    )

                    "Wind Speed Unit" -> viewModel.saveSetting(
                        SettingsDataStore.WIND_SPEED_UNIT_KEY,
                        value
                    )

                    "Location Method" -> viewModel.saveSetting(
                        SettingsDataStore.LOCATION_METHOD_KEY,
                        value
                    )
                }
            }
        }
    }
}

@Composable
fun SettingSection(
    title: String,
    options: List<String>,
    selectedOptions: MutableMap<String, String>,
    onOptionSelected: (String, String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontSize = 16.sp, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.selectableGroup()) {
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (option == selectedOptions[title]),
                                onClick = {
                                    selectedOptions[title] = option
                                    onOptionSelected(title, option)
                                },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = (option == selectedOptions[title]),
                            onClick = {
                                selectedOptions[title] = option
                                onOptionSelected(title, option)
                            }
                        )
                        Text(
                            text = option,
                            modifier = Modifier.padding(start = 8.dp),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
object LanguageConverter {
    fun changeLanguage(context: Context, languageCode: String) {
        //version >= 13
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(languageCode)
        } else {
            //version < 13
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
        }
    }

    fun formatNumber(value: Int): String {
        val formatter = NumberFormat.getInstance(Locale.getDefault())
        return formatter.format(value)
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = Color(0xFFC5E2EE),
        tonalElevation = 8.dp,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
    ) {
        val selectedItemColor = Color.Black
        val unselectedItemColor = Color.LightGray

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color.Black) },
            label = { Text("Home", color = Color.Black) },
            selected = true,
            onClick = { navController.navigate("Home") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.White,
                selectedIconColor = selectedItemColor,
                unselectedIconColor = unselectedItemColor,
                selectedTextColor = selectedItemColor,
                unselectedTextColor = unselectedItemColor
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorites", tint = Color.Black) },
            label = { Text("Favorites", color = Color.Black) },
            selected = false,
            onClick = { navController.navigate("Favorite") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.White,
                selectedIconColor = selectedItemColor,
                unselectedIconColor = unselectedItemColor,
                selectedTextColor = selectedItemColor,
                unselectedTextColor = unselectedItemColor
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Notifications, contentDescription = "Notifications", tint = Color.Black) },
            label = { Text("Notifications", color = Color.Black) },
            selected = false,
            onClick = { navController.navigate("Alert") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.White,
                selectedIconColor = selectedItemColor,
                unselectedIconColor = unselectedItemColor,
                selectedTextColor = selectedItemColor,
                unselectedTextColor = unselectedItemColor
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = Color.Black) },
            label = { Text("Settings", color = Color.Black) },
            selected = false,
            onClick = { navController.navigate("Setting") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.White,
                selectedIconColor = selectedItemColor,
                unselectedIconColor = unselectedItemColor,
                selectedTextColor = selectedItemColor,
                unselectedTextColor = unselectedItemColor
            )
        )
    }
}
