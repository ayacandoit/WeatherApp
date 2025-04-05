package com.example.weatherapp3.data.LocalDataSource

import com.example.weatherapp3.data.models.Alert
import com.example.weatherapp3.data.models.FavoriteLocation
import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(private val dao: FavoriteDao):LocalDataSource {
    override suspend fun getAllLocations(): Flow<List<FavoriteLocation>> {
        return dao.getAllLocations()
    }

    override suspend fun insertLocation(location: FavoriteLocation) {
        return dao.insertLocation(location)
    }

    override suspend fun deleteLocation(location: FavoriteLocation) {
        return dao.deleteLocation(location)
    }


}