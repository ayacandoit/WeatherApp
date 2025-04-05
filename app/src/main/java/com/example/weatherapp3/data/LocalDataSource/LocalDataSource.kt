package com.example.weatherapp3.data.LocalDataSource

import com.example.weatherapp3.data.models.Alert
import com.example.weatherapp3.data.models.FavoriteLocation
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun getAllLocations(): Flow<List<FavoriteLocation>>
    suspend fun insertLocation(location: FavoriteLocation)
    suspend fun deleteLocation(location: FavoriteLocation)



}