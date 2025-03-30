package com.example.weatherapp3.FavoriteLocation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp3.data.models.FavoriteLocation
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
// Add these imports at the top of your MapScreen.kt file
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
@Composable
fun MapScreen(
    onBack: () -> Unit,
    onLocationSaved: (FavoriteLocation) -> Unit
) {
    var locationName by remember { mutableStateOf("") }
    val cameraPositionState = rememberCameraPositionState()
    var selectedPosition by remember { mutableStateOf<LatLng?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                selectedPosition = latLng
            }
        ) {
            selectedPosition?.let { latLng ->
                Marker(
                    state = MarkerState(position = latLng),
                    title = "Selected Location"
                )
            }
        }

        TextField(
            value = locationName,
            onValueChange = { locationName = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Enter location name") }
        )

        FloatingActionButton(
            onClick = {
                selectedPosition?.let {
                    onLocationSaved(
                        FavoriteLocation(
                            name = locationName.ifEmpty { "Custom Location" },
                            latitude = it.latitude,
                            longitude = it.longitude
                        )
                    )
                    onBack()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("Save Location")
        }
    }
}