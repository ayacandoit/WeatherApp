package com.example.weatherapp3.FavoriteLocation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.weatherapp3.data.models.FavoriteLocation
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
// Add these imports at the top of your MapScreen.kt file
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import kotlinx.coroutines.tasks.await

@Composable
fun MapScreen(
    onBack: () -> Unit,
    onLocationSaved: (FavoriteLocation) -> Unit
) {
    val context = LocalContext.current
    // Initialize Places API client
    if (!Places.isInitialized()) {
        Places.initialize(context, "AIzaSyAAtHDDpgIrJdxASUWhG6YQNeuwxnhfHj0")
    }
    val placesClient = remember { Places.createClient(context) }

    // State variables
    var searchQuery by remember { mutableStateOf("") }
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    val cameraPositionState = rememberCameraPositionState()

    // Search predictions effect
    LaunchedEffect(searchQuery) {
        if (searchQuery.length > 3) {
            try {
                val request = FindAutocompletePredictionsRequest.builder()
                    .setQuery(searchQuery)
                    .build()

                val response = placesClient.findAutocompletePredictions(request).await()
                predictions = response.autocompletePredictions
            } catch (e: Exception) {
                predictions = emptyList()
            }
        } else {
            predictions = emptyList()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Google Map
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                selectedLocation = latLng
                searchQuery = "Selected Location"
            }
        ) {
            selectedLocation?.let { latLng ->
                Marker(
                    state = MarkerState(position = latLng),
                    title = searchQuery.ifEmpty { "Custom Location" }
                )
            }
        }

        // Search UI
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search location...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            // Search Predictions
            if (predictions.isNotEmpty()) {
                PredictionsList(
                    predictions = predictions,
                    placesClient = placesClient,
                    onPredictionClick = { prediction ->
                        placesClient.fetchPlace(
                            FetchPlaceRequest.builder(
                                prediction.placeId,
                                listOf(Place.Field.LAT_LNG, Place.Field.NAME)
                            ).build()
                        ).addOnSuccessListener { response ->
                            val place = response.place
                            selectedLocation = place.latLng
                            searchQuery = place.name ?: "Selected Location"
                            predictions = emptyList()
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                                place.latLng!!,
                                15f
                            )
                        }.addOnFailureListener {
                            predictions = emptyList()
                        }
                    }
                )
            }
        }

        // Save Button
        FloatingActionButton(
            onClick = {
                selectedLocation?.let { latLng ->
                    onLocationSaved(
                        FavoriteLocation(
                            name = searchQuery.ifEmpty { "Custom Location" },
                            latitude = latLng.latitude,
                            longitude = latLng.longitude
                        )
                    )
                    onBack()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Text("Save Location", color = Color.White)
        }
    }
}

@Composable
private fun PredictionsList(
    predictions: List<AutocompletePrediction>,
    placesClient: com.google.android.libraries.places.api.net.PlacesClient,
    onPredictionClick: (AutocompletePrediction) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        LazyColumn(
            modifier = Modifier.background(Color.White)
        ) {
            items(predictions) { prediction ->
                PredictionItem(
                    prediction = prediction,
                    onClick = { onPredictionClick(prediction) }
                )
                Divider(color = Color.LightGray, thickness = 0.5.dp)
            }
        }
    }
}

@Composable
private fun PredictionItem(
    prediction: AutocompletePrediction,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = prediction.getFullText(null).toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
