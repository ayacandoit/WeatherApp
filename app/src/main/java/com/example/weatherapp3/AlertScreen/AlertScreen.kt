// AlertScreen.kt
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.weatherapp3.AlertScreen.AlertViewModel
import com.example.weatherapp3.AlertScreen.AlertViewModelFactory
import com.example.weatherapp3.R
import com.example.weatherapp3.data.LocalDataSource.WeatherAlertDatabase
import com.example.weatherapp3.data.models.Alert
import com.example.weatherapp3.data.repository.AlertRepositoryImpl
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherAlertsScreen(navController: NavController) {
    val context = LocalContext.current
    val database = WeatherAlertDatabase.getDatabase(context)
    val repository = AlertRepositoryImpl(database.weatherAlertDao())
    val viewModel: AlertViewModel = viewModel(
        factory = AlertViewModelFactory(repository)
    )

    val alerts by viewModel.alerts.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color(0xFF64B5F6)
            ) {
                Icon(Icons.Default.Add, "Add Alert", tint = Color.White)
            }
        },
        bottomBar = { BottomNaigationBar(navController) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (alerts.isEmpty()) {
                EmptyAlertsList()
            } else {
                AlertsList(alerts, viewModel::removeAlert)
            }

            if (showDialog) {
                AddAlertDialog(
                    onDismiss = { showDialog = false },
                    onSave = { alert ->
                        viewModel.addAlert(alert)
                    }
                )
            }
        }
    }
}

@Composable
private fun EmptyAlertsList() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("No alerts found", color = Color.Gray)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun AlertsList(alerts: List<Alert>, onDelete: (Alert) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(alerts) { alert ->
            AlertItem(alert, onDelete)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlertItem(alert: Alert, onDelete: (Alert) -> Unit) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val date = Instant.ofEpochMilli(alert.date)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val startTime = Instant.ofEpochMilli(alert.startTime)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()

    val endTime = Instant.ofEpochMilli(alert.endTime)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB3E5FC))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = date.format(dateFormatter),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${startTime.format(timeFormatter)} - ${endTime.format(timeFormatter)}",
                    color = Color.Gray
                )
                Text(
                    text = alert.location,
                    color = Color.Black
                )
            }
            IconButton(onClick = { onDelete(alert) }) {
                Icon(
                    painter = painterResource(R.drawable.wind),
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlertDialog(
    onDismiss: () -> Unit,
    onSave: (Alert) -> Unit
) {
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var startTime by remember { mutableStateOf(0L) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Weather Alert") },
        text = {
            Column {
                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Date")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { showTimePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Start Time")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val calendar = Calendar.getInstance().apply {
                        timeInMillis = selectedDate
                    }

                    onSave(
                        Alert(
                            date = calendar.timeInMillis,
                            startTime = startTime,
                            endTime = startTime + 3600000, // +1 hour
                            location = "Current Location"
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog1(
            onDateSelected = { selectedDate = it },
            onDismiss = { showDatePicker = false }
        )
    }

    if (showTimePicker) {
        TimePickerDialog(
            initialTimeMillis = startTime,
            onTimeSelected = { startTime = it },
            onDismiss = { showTimePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog1(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = {
                showDialog = false
                onDismiss()
            },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let(onDateSelected)
                        showDialog = false
                        onDismiss()
                    }
                ) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    initialTimeMillis: Long,
    onTimeSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = Calendar.getInstance().apply { timeInMillis = initialTimeMillis }
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE)
    )
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        androidx.compose.material3.
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                onDismiss()}
        ){
            Column {

                TimePicker(state = timePickerState)
                Button(
                    onClick = {
                        val millis =
                            (timePickerState.hour * 3600 + timePickerState.minute * 60) * 1000L
                        onTimeSelected(millis)
                        showDialog = false
                        onDismiss()
                    }
                ) {
                    Text("OK")
                }

            }
        }
    }
}

@Composable
fun BottomNaigationBar(navController: NavController) {
    NavigationBar(
        containerColor = Color(0xFFC5E2EE),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, "Home") },
            label = { Text("Home") },
            selected = false,
            onClick = { navController.navigate("Home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, "Favorites") },
            label = { Text("Favorites") },
            selected = true,
            onClick = { navController.navigate("Favorite") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Notifications, "Alerts") },
            label = { Text("Alerts") },
            selected = false,
            onClick = { navController.navigate("Alert") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, "Settings") },
            label = { Text("Settings") },
            selected = false,
            onClick = { navController.navigate("Setting") }
        )
    }
}