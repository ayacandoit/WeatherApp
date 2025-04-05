package com.example.weatherapp3.data.repository

import com.example.weatherapp3.data.LocalDataSource.AlertDao
import com.example.weatherapp3.data.models.Alert
import kotlinx.coroutines.flow.Flow

class AlertRepositoryImpl(private val dao: AlertDao) : AlertRepository {
    override suspend fun addAlert(alert: Alert): Long = dao.insert(alert)
    override suspend fun removeAlert(alert: Alert) = dao.delete(alert)
    override fun getAllAlerts(): Flow<List<Alert>> = dao.getAllAlerts()
}