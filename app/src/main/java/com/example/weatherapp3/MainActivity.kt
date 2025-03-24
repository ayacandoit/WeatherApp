package com.example.weatherapp3



import FavoriteScreen
import WeatherAlertsScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
       var t= listOf{"vitnam"}
        setContent {
            Nav()


        }
    }
}



