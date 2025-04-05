import java.util.concurrent.TimeUnit

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: Double, lon: Double,unit: String ): Result<WeatherResponse>
    suspend fun getForecast(lat: Double, lon: Double): Result<ForecastResponse>

    sealed interface Result<out T> {
        data class Success<T>(val data: T) : Result<T>
        data class Error(val exception: Throwable) : Result<Nothing>
    }
}
