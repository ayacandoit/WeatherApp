package com.example.weatherapp3.AlertScreen
import WeatherResponse
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import androidx.work.CoroutineWorker
import kotlinx.coroutines.launch
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.time.Duration
import java.time.LocalDateTime
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import com.example.weatherapp3.data.repository.AlertRepository
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel


import androidx.lifecycle.viewModelScope
import com.example.weatherapp3.data.models.Alert
import kotlinx.coroutines.launch

import androidx.lifecycle.viewModelScope
import androidx.work.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.random.Random


class AlertViewModel(private val repository: AlertRepository) : ViewModel() {
    val alerts = repository.getAllAlerts().asLiveData()
    private val TAG = "AlertViewModel"

    fun addAlert(alert: Alert) {
        viewModelScope.launch {
            val workId = scheduleNotification(alert)
            val newAlert = alert.copy(workId = workId.toString())
            repository.addAlert(newAlert)
        }
    }

    fun removeAlert(alert: Alert) {
        viewModelScope.launch {
            alert.workId?.let { cancelNotification(UUID.fromString(it)) }
            repository.removeAlert(alert)
        }
    }

    private fun scheduleNotification(alert: Alert): UUID {
        val delay = calculateDelay(alert.date, alert.startTime)
        Log.i(TAG, "scheduleNotification:$delay ")
        val workRequest = OneTimeWorkRequestBuilder<AlertWorker>()
            .setInputData(createInputData(alert))
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance().enqueue(workRequest)
        return workRequest.id
    }

    private fun cancelNotification(workId: UUID) {
        WorkManager.getInstance().cancelWorkById(workId)
    }

    private fun calculateDelay(date: Long, startTime: Long): Long {
        val alertDateTime = date + startTime
        val currentTime = System.currentTimeMillis()
        return alertDateTime/1000 - currentTime
    }

    private fun createInputData(alert: Alert): Data {
        return workDataOf(
            "location" to alert.location,
            "startTime" to alert.startTime,
            "endTime" to alert.endTime
        )
    }
}

class AlertWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val apiService by lazy { createWeatherApiService() }

    override suspend fun doWork(): Result {
        return try {
            val location = inputData.getString("location") ?: return Result.failure()
            val weatherData = getWeatherData(location)
            sendNotification(weatherData)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private suspend fun getWeatherData(location: String): WeatherResponse {
        val (lat, lon) = location.split(",").map { it.toDouble() }
        return apiService.getCurrentWeather(lat, lon, "metric", "7aa3bb648428835511de89e79afe24c5")
    }

    private fun sendNotification(weather: WeatherResponse) {
        NotificationHelper(applicationContext).sendNotification(
            title = "Weather Alert: ${weather.name}",
            message = "Current: ${weather.weather[0].description}\nTemp: ${weather.main.temp}°C"
        )
    }

    private fun createWeatherApiService(): WeatherApi.WeatherApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi.WeatherApiService::class.java)
    }
}

class NotificationHelper(private val context: Context) {
    private val channelId = "weather_alerts_channel"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Weather Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Weather alert notifications"
            }
            context.getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }

    fun sendNotification(title: String, message: String) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        context.getSystemService(NotificationManager::class.java)?.notify(
            Random.nextInt(),
            notification
        )
    }
}
class AlertViewModelFactory(
    private val repository: AlertRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            return AlertViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



