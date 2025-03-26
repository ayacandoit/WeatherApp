package com.example.weatherapp3

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.*



@Composable
fun WeatherScreen(
    navController: NavController,
    location: Location?,
    address: String,
    showPermissionDialog: Boolean,
    onRequestPermission: () -> Unit,
    onDismissPermissionDialog: () -> Unit
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.drawable.skky),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                WeatherCard(
                    location = location,
                    address = address
                )

                Text(
                    text = "Today",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )

                WeatherRow()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Next 5 days",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow,
                    modifier = Modifier.padding(16.dp)
                )

                repeat(5) { index ->
                    WeatherListItem(
                        day = "Day ${index + 1}",
                        temperature = "${(15..25).random()}°C",
                        weatherCondition = listOf("Clear", "Cloudy", "Rainy", "Snowy", "Windy").random()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (showPermissionDialog) {
            AlertDialog(
                onDismissRequest = onDismissPermissionDialog,
                title = { Text("Location Permission Required") },
                text = { Text("This app needs location access to provide accurate weather information") },
                confirmButton = {
                    Button(onClick = {
                        onRequestPermission()
                        onDismissPermissionDialog()
                    }) {
                        Text("Allow")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismissPermissionDialog) {
                        Text("Deny")
                    }
                }
            )
        }
    }
}

@Composable
fun WeatherCard(
    location: Location?,
    address: String
) {
    val temperature = location?.let { "%.1f°C".format(20.0 + it.latitude % 10) } ?: "--°C"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB3E5FC).copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.clearsky),
                contentDescription = null,
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = address,
                color = Color.White,
            )

            Text(
                text = temperature,
                color = Color.White,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = "Today",
                color = Color.Gray,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherInfoItem(icon = R.drawable.clouds, label = "Cloud", value = "0%")
                WeatherInfoItem(icon = R.drawable.wind, label = "Wind speed", value = "4.15 m/s")
                WeatherInfoItem(icon = R.drawable.humidity, label = "Humidity", value = "68")
                WeatherInfoItem(icon = R.drawable.pressure, label = "Pressure", value = "1014")
            }
        }
    }
}

@Composable
fun WeatherListItem(day: String, temperature: String, weatherCondition: String) {
    val weatherIcon = when (weatherCondition) {
        "Clear" -> R.drawable.clearsky
        "Cloudy" -> R.drawable.clouds
        "Rainy" -> R.drawable.heavyrain
        "Snowy" -> R.drawable.snowflake
        "Windy" -> R.drawable.wind
        else -> R.drawable.wind
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB3E5FC).copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(weatherIcon),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Text(text = day, color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = temperature, color = Color.White, fontSize = 18.sp)
            Text(text = weatherCondition, color = Color.Gray)
        }
    }
}

@Composable
fun WeatherRow() {
    LazyRow(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(5) { index ->
            WeatherItem(
                time = "${(index + 6) * 3}:00",
                temperature = "${(10..15).random()}.0°C",
                weatherCondition = listOf("Clear", "Cloudy", "Rainy", "Snowy", "Windy").random()
            )
        }
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
            onClick = {navController.navigate("Home")},
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
            onClick = {navController.navigate("Favorite")},
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
            onClick = {navController.navigate("Alert")},
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
            onClick = {navController.navigate("Setting")},
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

@Composable
fun WeatherItem(time: String, temperature: String, weatherCondition: String) {
    val weatherIcon = when (weatherCondition) {
        "Clear" -> R.drawable.clearsky
        "Cloudy" -> R.drawable.clouds
        "Rainy" -> R.drawable.heavyrain
        "Snowy" -> R.drawable.snowflake
        "Windy" -> R.drawable.wind
        else -> R.drawable.wind
    }

    Card(
        modifier = Modifier
            .width(80.dp)
            .height(180.dp),
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB3E5FC).copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = time,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Icon(
                painter = painterResource(weatherIcon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = temperature,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun WeatherInfoItem(icon: Int, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Text(text = label, color = Color.Gray)
        Text(text = value, color = Color.White)
    }
}