package com.example.weatherapp3

import com.example.weatherapp3.data.LocalDataSource.LocalDataSource
import com.example.weatherapp3.data.models.FavoriteLocation
import kotlinx.coroutines.flow.Flow
// FakeLocalDataSource.kt


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeLocalDataSource : LocalDataSource {

    private val _locations = MutableStateFlow<List<FavoriteLocation>>(emptyList())

    override suspend fun getAllLocations(): Flow<List<FavoriteLocation>> {
        return _locations
    }

    override suspend fun insertLocation(location: FavoriteLocation) {
        _locations.update { current ->
            current + location
        }
    }

    override suspend fun deleteLocation(location: FavoriteLocation) {
        _locations.update { current ->
            current.filter { it.id != location.id }
        }
    }
}