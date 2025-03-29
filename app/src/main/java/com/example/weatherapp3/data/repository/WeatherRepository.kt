class WeatherRepository {
    private val apiService = WeatherApi.retrofitService

    suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherResponse {
        return apiService.getCurrentWeather(lat, lon)
    }

    suspend fun getForecast(lat: Double, lon: Double): ForecastResponse {
        return apiService.getForecast(lat, lon)
    }
}