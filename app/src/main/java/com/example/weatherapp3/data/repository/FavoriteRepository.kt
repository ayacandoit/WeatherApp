package com.example.weatherapp3.data.repository

import com.example.weatherapp3.data.LocalDataSource.FavoriteDao
import com.example.weatherapp3.data.models.FavoriteLocation
import kotlinx.coroutines.flow.Flow

interface IFavoriteRepository {
    suspend fun getAllLocations(): Flow<List<FavoriteLocation>>
    suspend fun addLocation(location: FavoriteLocation)
    suspend fun removeLocation(location: FavoriteLocation)
}