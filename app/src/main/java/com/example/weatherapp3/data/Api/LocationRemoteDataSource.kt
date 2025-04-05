package com.example.weatherapp3.data.Api

import ForecastResponse
import WeatherResponse
import retrofit2.http.Query

interface LocationRemoteDataSource {
    suspend fun getCurrentWeather(lat: Double, lon: Double,unit: String ): WeatherResponse
    suspend fun getForecast(lat: Double, lon: Double): ForecastResponse



}