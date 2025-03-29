package com.example.weatherapp.ui.screens

import WeatherResponse
import WeatherViewModel
import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weatherapp3.R

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WeatherScreen(
    navController: NavController,
    location: Location?,
    address: String,
    showPermissionDialog: Boolean,
    onRequestPermission: () -> Unit,
    onDismissPermissionDialog: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: WeatherViewModel = remember {
        ViewModelProvider.provideWeatherViewModel(context)
    }

    val isLoading by viewModel.isLoading.collectAsState()
    val currentWeather by viewModel.currentWeather.collectAsState()
    val forecast by viewModel.forecast.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(location) {
        location?.let {
            viewModel.fetchWeatherData(it.latitude, it.longitude)
        }
    }

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

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }

            error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                WeatherCard(
                    location = location,
                    address = address,
                    currentWeather = currentWeather
                )

                Text(
                    text = "Today",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )

                currentWeather?.let { weather ->
                    WeatherRow(weather = weather)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Next 5 days",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow,
                    modifier = Modifier.padding(16.dp)
                )

                forecast?.let { forecastItems ->
                    forecastItems.groupBy {
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .format(Date(it.dt * 1000))
                    }.values.take(5).forEach { dailyForecasts ->
                        val firstForecast = dailyForecasts.first()
                        WeatherListItem(
                            day = SimpleDateFormat("EEEE", Locale.getDefault())
                                .format(Date(firstForecast.dt * 1000)),
                            temperature = "${firstForecast.main.temp.toInt()}°C",
                            weatherCondition = firstForecast.weather.firstOrNull()?.main ?: "Unknown",
                            iconCode = firstForecast.weather.firstOrNull()?.icon ?: "01d"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                } ?: run {
                    repeat(5) { index ->
                        WeatherListItem(
                            day = "Day ${index + 1}",
                            temperature = "${(15..25).random()}°C",
                            weatherCondition = listOf("Clear", "Cloudy", "Rainy", "Snowy", "Windy").random(),
                            iconCode = "01d"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
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
    address: String,
    currentWeather: WeatherResponse?
) {
    val temperature = currentWeather?.main?.temp?.let { "%.1f°C".format(it) } ?: "--°C"
    val weatherCondition = currentWeather?.weather?.firstOrNull()?.main ?: "Clear"
    val iconCode = currentWeather?.weather?.firstOrNull()?.icon ?: "01d"

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
            WeatherIcon(
                iconCode = iconCode,
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
                text = weatherCondition,
                color = Color.Gray,
            )

            Spacer(modifier = Modifier.height(16.dp))

            currentWeather?.let { weather ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherInfoItem(
                        icon = R.drawable.clouds,
                        label = "Cloud",
                        value = "${weather.clouds.all}%"
                    )
                    WeatherInfoItem(
                        icon = R.drawable.wind,
                        label = "Wind speed",
                        value = "${weather.wind.speed} m/s"
                    )
                    WeatherInfoItem(
                        icon = R.drawable.humidity,
                        label = "Humidity",
                        value = "${weather.main.humidity}%"
                    )
                    WeatherInfoItem(
                        icon = R.drawable.pressure,
                        label = "Pressure",
                        value = "${weather.main.pressure} hPa"
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherListItem(
    day: String,
    temperature: String,
    weatherCondition: String,
    iconCode: String
) {
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
            WeatherIcon(
                iconCode = iconCode,
                modifier = Modifier.size(40.dp)
            )
            Text(text = day, color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = temperature, color = Color.White, fontSize = 18.sp)
            Text(text = weatherCondition, color = Color.Gray)
        }
    }
}

@Composable
fun WeatherRow(weather: WeatherResponse) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(5) { index ->
            val hour = (index + 1) * 3
            WeatherItem(
                time = "$hour:00",
                temperature = "${weather.main.temp.toInt() + (index - 2)}°C",
                iconCode = weather.weather.firstOrNull()?.icon ?: "01d"
            )
        }
    }
}

@Composable
fun WeatherItem(
    time: String,
    temperature: String,
    iconCode: String
) {
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
            WeatherIcon(
                iconCode = iconCode,
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
fun WeatherIcon(iconCode: String, modifier: Modifier = Modifier) {
    val iconUrl = "https://openweathermap.org/img/wn/$iconCode@2x.png"

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(iconUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = modifier,
        colorFilter = if (iconCode.endsWith("n"))
            ColorFilter.tint(Color(0xFF90CAF9)) else null
    )
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