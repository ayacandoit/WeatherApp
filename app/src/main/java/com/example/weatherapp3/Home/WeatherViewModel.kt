import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp3.data.Api.RemoteDataSourceImp
import com.example.weatherapp3.data.repository.WeatherRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentWeather = MutableStateFlow<WeatherResponse?>(null)
    val currentWeather: StateFlow<WeatherResponse?> = _currentWeather.asStateFlow()

    private val _forecast = MutableStateFlow<List<ForecastItem>?>(null)
    val forecast: StateFlow<List<ForecastItem>?> = _forecast.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchWeatherData(lat: Double, lon: Double,unit:String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            when (val currentWeatherResult = repository.getCurrentWeather(lat, lon,unit)) {
                is WeatherRepository.Result.Success -> {
                    _currentWeather.value = currentWeatherResult.data
                }
                is WeatherRepository.Result.Error -> {
                    _error.value = "Current weather: ${currentWeatherResult.exception.message}"
                }

                else -> {}
            }

            when (val forecastResult = repository.getForecast(lat, lon)) {
                is WeatherRepository.Result.Success -> {
                    _forecast.value = forecastResult.data.list
                }
                is WeatherRepository.Result.Error -> {
                    _error.value = _error.value?.let { "$it\nForecast: ${forecastResult.exception.message}" }
                        ?: "Forecast: ${forecastResult.exception.message}"
                }

                else -> {}
            }

            _isLoading.value = false
        }
    }
}
class WeatherViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
object ViewModelProvider {
    fun provideWeatherViewModel(context: Context): WeatherViewModel {
        val repository = WeatherRepositoryImpl(RemoteDataSourceImp(WeatherApi.service))
        val factory = WeatherViewModelFactory(repository)
        return ViewModelProvider(
            (context as ComponentActivity).viewModelStore,
            factory
        ).get(WeatherViewModel::class.java)
    }
}