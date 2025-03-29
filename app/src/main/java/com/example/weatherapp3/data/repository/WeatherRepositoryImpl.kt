package com.example.weatherapp3.data.repository

import ForecastResponse
import WeatherRepository
import WeatherResponse
class WeatherRepositoryImpl(
    private val apiService: WeatherApi.WeatherApiService
) : WeatherRepository {

    override suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherRepository.Result<WeatherResponse> {
        return try {
            val response = apiService.getCurrentWeather(lat, lon)
            WeatherRepository.Result.Success(response)
        } catch (e: Exception) {
            WeatherRepository.Result.Error(e)
        }
    }

    override suspend fun getForecast(lat: Double, lon: Double): WeatherRepository.Result<ForecastResponse> {
        return try {
            val response = apiService.getForecast(lat, lon)
            WeatherRepository.Result.Success(response)
        } catch (e: Exception) {
            WeatherRepository.Result.Error(e)
        }
    }
}