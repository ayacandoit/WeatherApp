package com.example.weatherapp3.FavoriteLocation

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp3.data.models.FavoriteLocation
import com.example.weatherapp3.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
class FavoriteViewModel(private val repository: FavoriteRepository) : ViewModel() {
    var isLoading by mutableStateOf(true)
        private set

    var favoriteLocations by mutableStateOf(emptyList<FavoriteLocation>())
        private set

    var showEmptyState by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadLocations()
    }

    private fun loadLocations() {
        viewModelScope.launch {
            try {
                repository.allLocations.collect { locations ->
                    isLoading = false
                    favoriteLocations = locations
                    showEmptyState = locations.isEmpty()
                }
            } catch (e: Exception) {
                isLoading = false
                errorMessage = "Failed to load locations: ${e.message}"
            }
        }
    }

    fun addLocation(location: FavoriteLocation) {
        viewModelScope.launch {
            try {
                repository.addLocation(location)
            } catch (e: Exception) {
                errorMessage = "Failed to save location: ${e.message}"
            }
        }
    }

    fun deleteLocation(location: FavoriteLocation) {
        viewModelScope.launch {
            try {
                repository.removeLocation(location)
            } catch (e: Exception) {
                errorMessage = "Failed to delete location: ${e.message}"
            }
        }
    }
}