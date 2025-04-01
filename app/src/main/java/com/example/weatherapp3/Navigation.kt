package com.example.weatherapp3

import FavoriteScreen
import SettingsScreen
import WeatherAlertsScreen
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.ui.screens.WeatherScreen
import com.example.weatherapp3.FavoriteLocation.FavoriteViewModel
import com.example.weatherapp3.FavoriteLocation.FavoriteViewModelFactory
import com.example.weatherapp3.FavoriteLocation.MapScreen
import com.example.weatherapp3.data.LocalDataSource.AppDatabase
import com.example.weatherapp3.data.repository.FavoriteRepository



// Navigation.kt
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
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val repository = remember { FavoriteRepository(db.favoriteDao()) }

    NavHost(navController = navController, startDestination = "Splash") {
        composable(route = "Splash") {
            SplashScreen(navController)
        }
        composable(route = "Favorite") {
            FavoriteScreen(navController)
        }
        composable(route = "MapScreen") {
            val viewModel: FavoriteViewModel = viewModel(
                factory = FavoriteViewModelFactory(repository)
            )
            MapScreen(
                onBack = { navController.popBackStack() },
                onLocationSaved = { location ->
                    viewModel.addLocation(repository, location)
                }
            )
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