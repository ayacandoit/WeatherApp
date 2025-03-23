package com.example.weatherapp3

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp3.R
@Composable
fun WeatherScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // صورة الخلفية
        Image(
            painter = painterResource(R.drawable.skky),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WeatherCard(weatherCondition = "Clear")
        }
    }
}

@Composable
fun WeatherCard(weatherCondition: String) {
    val weatherIcon = when (weatherCondition) {
        "Clear" -> R.drawable.clearsky
        "Cloudy" -> R.drawable.clouds
        "Rainy" -> R.drawable.heavyrain
        "Snowy" -> R.drawable.snowflake
        "Windy" -> R.drawable.wind
        else->R.drawable.wind
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(weatherIcon),
                contentDescription = null,
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = weatherCondition,
                color = Color.White,
            )

            Text(
                text = "61.18°F",
                color = Color.White,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = "2024-04-09",
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

@Preview(showBackground = true)
@Composable
fun PreviewWeatherScreen() {
    WeatherScreen()
}




