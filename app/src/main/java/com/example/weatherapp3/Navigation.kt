package com.example.weatherapp3

import FavoriteScreen
import MapScreen
import SettingsScreen
import WeatherAlertsScreen
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weatherapp.ui.screens.WeatherScreen
import com.example.weatherapp3.FavoriteLocation.FavoriteViewModel
import com.example.weatherapp3.FavoriteLocation.FavoriteViewModelFactory
import com.example.weatherapp3.data.LocalDataSource.AppDatabase
import com.example.weatherapp3.data.repository.FavoriteRepository


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

    NavHost(
        navController = navController,
        startDestination = "Splash"
    ) {
        composable("Splash") {
            SplashScreen(navController)
        }

        composable("Favorite") {
            FavoriteScreen(navController)
        }

        composable("MapScreen") {
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

        composable("Alert") {
            WeatherAlertsScreen(navController)
        }

        composable(
            "weather/{lat}/{lon}/{name}",
            arguments = listOf(
                navArgument("lat") { type = NavType.FloatType },
                navArgument("lon") { type = NavType.FloatType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val lat = backStackEntry.arguments?.getFloat("lat")?.toDouble()
            val lon = backStackEntry.arguments?.getFloat("lon")?.toDouble()
            val name = backStackEntry.arguments?.getString("name") ?: ""

            val customLocation = Location(name).apply {
                latitude = lat ?: 0.0
                longitude = lon ?: 0.0
            }

            WeatherScreen(
                navController = navController,
                location = customLocation,
                address = name,
                showPermissionDialog = false,
                onRequestPermission = onRequestPermission,
                onDismissPermissionDialog = onDismissPermissionDialog
            )
        }

        composable("Home") {
            WeatherScreen(
                navController = navController,
                location = currentLocation,
                address = address,
                showPermissionDialog = showPermissionDialog,
                onRequestPermission = onRequestPermission,
                onDismissPermissionDialog = onDismissPermissionDialog
            )
        }

        composable("Setting") {
            SettingsScreen(navController)
        }
    }
}