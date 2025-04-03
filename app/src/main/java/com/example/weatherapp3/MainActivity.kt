package com.example.weatherapp3




import WeatherAlertsScreen
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocation by mutableStateOf<Location?>(null)
    private var address by mutableStateOf("Loading location...")
    private var showPermissionDialog by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            Nav(
                onRequestPermission = { requestLocationPermissions() },
                onEnableLocation = { enableLocationServices() },
                currentLocation = currentLocation,
                address = address,
                showPermissionDialog = showPermissionDialog,
                onDismissPermissionDialog = { showPermissionDialog = false }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkPermission() && isLocationEnabled()) {
            getLocation()
        } else if (!checkPermission()) {
            showPermissionDialog = true
        }
    }

    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
            ),
            REQUEST_LOCATION_CODE
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (!checkPermission()) return

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                currentLocation = it
                address = getAddressFromLocation(it, this)
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).build(),
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    currentLocation = locationResult.lastLocation
                    currentLocation?.let { address = getAddressFromLocation(it, this@MainActivity) }
                }
            },
            mainLooper
        )
    }

    private fun enableLocationServices() {
        Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show()
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun getAddressFromLocation(location: Location, context: Context): String {
        return try {
            val geocoder = Geocoder(context)
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown location"
        } catch (e: Exception) {
            "Location: ${location.latitude}, ${location.longitude}"
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if(requestCode== REQUEST_LOCATION_CODE){
            if(grantResults.get(0)==PackageManager.PERMISSION_GRANTED || grantResults.get(1)==PackageManager.PERMISSION_GRANTED){
                if(isLocationEnabled()){
                    getLocation()
                }else{
                    enableLocationServices()
                }
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION_CODE = 1001
    }
}



