package com.example.weatherapp3

import FavoriteScreen
import SettingsScreen
import WeatherAlertsScreen
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.ui.screens.WeatherScreen

@Composable
fun Nav(
    onRequestPermission: () -> Unit,
    onEnableLocation: () -> Unit,
    currentLocation: Location?,
    address: String,
    showPermissionDialog: Boolean,
    onDismissPermissionDialog: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Splash") {
        composable(route = "Splash") {
            SplashScreen(navController)
        }
        composable(route = "Favorite") {
            FavoriteScreen(navController)
        }
        composable(route = "Alert") {
            WeatherAlertsScreen(navController)
        }
        composable(route = "Home") {
            WeatherScreen(
                navController = navController,
                location = currentLocation,
                address = address,
                showPermissionDialog = showPermissionDialog,
                onRequestPermission = onRequestPermission,
                onDismissPermissionDialog = onDismissPermissionDialog
            )
        }
        composable(route = "Setting") {
            SettingsScreen(navController)
        }
    }
}
