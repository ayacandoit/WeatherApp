package com.example.weatherapp3.data.Api

import ForecastResponse
import WeatherApi
import WeatherResponse

class RemoteDataSourceImp(     val apiService: WeatherApi.WeatherApiService) :LocationRemoteDataSource {
    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        unit: String
    ): WeatherResponse{
        return apiService.getCurrentWeather(lat, lon)
    }

    override suspend fun getForecast(lat: Double, lon: Double): ForecastResponse {
        return apiService.getForecast(lat, lon)
    }

}