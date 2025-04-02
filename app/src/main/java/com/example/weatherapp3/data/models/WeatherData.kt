data class WeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val name: String
)

data class Coord(val lon: Double, val lat: Double)
data class Weather(val id: Int, val main: String, val description: String, val icon: String)
data class Main(val temp: Double, val pressure: Int, val humidity: Int, val temp_min: Double, val temp_max: Double)
data class Wind(val speed: Double, val deg: Int)
data class Clouds(val all: Int)
data class Sys(val country: String, val sunrise: Long, val sunset: Long)

data class ForecastResponse(
    val list: List<ForecastItem>,
    val city: City
)

data class ForecastItem(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val dt_txt: String
)

data class City(val name: String, val country: String)