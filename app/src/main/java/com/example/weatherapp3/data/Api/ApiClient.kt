package com.example.weatherapp3.data.Api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import coil.compose.AsyncImage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .build()
    }

    val weatherApi: WeatherApi.WeatherApiService by lazy {
        retrofit.create(WeatherApi.WeatherApiService::class.java)
    }
}
@Composable
fun WeatherIcon(iconCode: String, modifier: Modifier = Modifier) {
    val iconUrl = "https://openweathermap.org/img/wn/$iconCode@2x.png"

    AsyncImage(
        model = iconUrl,
        contentDescription = null,
        modifier = modifier,
        colorFilter = if (iconCode.endsWith("n"))
            ColorFilter.tint(Color(0xFF90CAF9)) else null
    )
}