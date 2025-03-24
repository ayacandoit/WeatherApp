package com.example.weatherapp3

import FavoriteScreen
import SettingsScreen
import WeatherAlertsScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Nav(){
    val navController= rememberNavController()
    NavHost(navController=navController, startDestination ="Splash" ){
        composable(route = "Splash"){
            SplashScreen(navController)
        }
        composable(route = "Favorite"){
            FavoriteScreen(navController)
        }
        composable(route = "Alert"){
            WeatherAlertsScreen(navController)
        }
        composable(route = "Home"){
            WeatherScreen(navController)
        }
        composable(route = "Setting"){
            SettingsScreen(navController)
        }

    }
}