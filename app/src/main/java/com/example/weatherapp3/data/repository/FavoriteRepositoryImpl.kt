package com.example.weatherapp3.data.repository

import com.example.weatherapp3.data.LocalDataSource.FavoriteDao
import com.example.weatherapp3.data.LocalDataSource.LocalDataSourceImpl
import com.example.weatherapp3.data.models.FavoriteLocation
import kotlinx.coroutines.flow.Flow
class FavoriteRepository (    private val localDataSource: LocalDataSourceImpl,
) : IFavoriteRepository {
    override suspend fun getAllLocations(): Flow<List<FavoriteLocation>> = localDataSource.getAllLocations()

    override suspend fun addLocation(location: FavoriteLocation) = localDataSource.insertLocation(location)
    override suspend fun removeLocation(location: FavoriteLocation) = localDataSource.deleteLocation(location)
}