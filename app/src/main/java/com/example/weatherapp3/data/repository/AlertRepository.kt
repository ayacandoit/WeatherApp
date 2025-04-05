package com.example.weatherapp3.data.repository


import android.Manifest
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.Flow
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.weatherapp3.data.LocalDataSource.AlertDao
import com.example.weatherapp3.data.models.Alert

interface AlertRepository {
    suspend fun addAlert(alert: Alert): Long
    suspend fun removeAlert(alert: Alert)
    fun getAllAlerts(): Flow<List<Alert>>
}