package com.example.weatherapp3.data.repository

import ForecastResponse
import WeatherRepository
import WeatherResponse
import com.example.weatherapp3.data.Api.RemoteDataSourceImp

class WeatherRepositoryImpl(
     val apiService: RemoteDataSourceImp
) : WeatherRepository {

    override suspend fun getCurrentWeather(lat: Double, lon: Double,unit:String): WeatherRepository.Result<WeatherResponse> {
        return try {
            val response = apiService.getCurrentWeather(lat, lon,unit)
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