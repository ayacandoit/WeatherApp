package com.example.weatherapp3.data.repository

import com.example.weatherapp3.data.LocalDataSource.FavoriteDao
import com.example.weatherapp3.data.models.FavoriteLocation
import kotlinx.coroutines.flow.Flow
class FavoriteRepository(private val dao: FavoriteDao) : IFavoriteRepository {
    override fun getAllLocations(): Flow<List<FavoriteLocation>> = dao.getAllLocations()
    override suspend fun addLocation(location: FavoriteLocation) = dao.insertLocation(location)
    override suspend fun removeLocation(location: FavoriteLocation) = dao.deleteLocation(location)
}