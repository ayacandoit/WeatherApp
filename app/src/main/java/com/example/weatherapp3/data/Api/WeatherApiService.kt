import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// network/WeatherApi.kt
object WeatherApi {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    interface WeatherApiService {
        @GET("weather")
        suspend fun getCurrentWeather(
            @Query("lat") lat: Double,
            @Query("lon") lon: Double,
            @Query("units") units: String = "metric",
            @Query("appid") apiKey: String ="7aa3bb648428835511de89e79afe24c5"
        ): WeatherResponse

        @GET("forecast")
        suspend fun getForecast(
            @Query("lat") lat: Double,
            @Query("lon") lon: Double,
            @Query("units") units: String = "metric",
            @Query("appid") apiKey: String ="7aa3bb648428835511de89e79afe24c5"
        ): ForecastResponse
    }

    val service: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}