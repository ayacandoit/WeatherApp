package com.example.weatherapp3.FavoriteLocation

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp3.data.models.FavoriteLocation
import com.example.weatherapp3.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

import androidx.lifecycle.viewModelScope
import com.example.weatherapp3.data.repository.IFavoriteRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
class FavoriteViewModel(repository: IFavoriteRepository) : ViewModel() {
    private val _locations = MutableStateFlow<List<FavoriteLocation>>(emptyList())
    val locations: StateFlow<List<FavoriteLocation>> = _locations

    private val _state = MutableStateFlow<UIState>(UIState.Loading)
    val state: StateFlow<UIState> = _state

    init {
        loadLocations(repository)
    }

    private fun loadLocations(repository: IFavoriteRepository) {
        viewModelScope.launch {
            repository.getAllLocations()
                .catch { e ->
                    _state.value = UIState.Error(e.message ?: "Unknown error")
                }
                .collect { locations ->
                    _locations.value = locations
                    _state.value = if (locations.isEmpty()) UIState.Empty else UIState.Success
                }
        }
    }

    fun addLocation(repository: IFavoriteRepository, location: FavoriteLocation) {
        viewModelScope.launch {
            try {
                repository.addLocation(location)
            } catch (e: Exception) {
                _state.value = UIState.Error("Failed to save location")
            }
        }
    }

    fun removeLocation(repository: IFavoriteRepository, location: FavoriteLocation) {
        viewModelScope.launch {
            try {
                repository.removeLocation(location)
            } catch (e: Exception) {
                _state.value = UIState.Error("Failed to delete location")
            }
        }
    }

    sealed class UIState {
        object Loading : UIState()
        object Success : UIState()
        object Empty : UIState()
        data class Error(val message: String) : UIState()
    }
}

class FavoriteViewModelFactory(private val repository: IFavoriteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}