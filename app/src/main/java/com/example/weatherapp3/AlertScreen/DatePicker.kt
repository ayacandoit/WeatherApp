import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.weatherapp3.data.models.Alert
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogExample(
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
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateSelected(it)
                    }
                    showDialog = false
                    onDismiss()
                }) {
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
fun TimePickerDialogExample(
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
        DatePickerDialog(
            onDismissRequest = {
                showDialog = false
                onDismiss()
            },
            confirmButton = {
                Button(onClick = {
                    val millis = (timePickerState.hour * 60 + timePickerState.minute) * 60 * 1000L
                    onTimeSelected(millis)
                    showDialog = false
                    onDismiss()
                }) {
                    Text("OK")
                }
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ((Alert) -> Unit).AddAlertDialog(
    onDismiss: () -> Unit
) {
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var startTime by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Weather Alert") },
        text = {
            Column {
                Button(onClick = { showDatePicker = true }) {
                    Text("Select Date")
                }
                Button(onClick = { showTimePicker = true }) {
                    Text("Select Start Time")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                this(
                    Alert(
                        date = selectedDate,
                        startTime = startTime,
                        endTime = startTime + 3600000, // +1 hour
                        location = "Current Location"
                    )
                )
                onDismiss()
            }) {
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
        DatePickerDialogExample(
            onDateSelected = { selectedDate = it },
            onDismiss = { showDatePicker = false }
        )
    }

    if (showTimePicker) {
        TimePickerDialogExample(
            initialTimeMillis = System.currentTimeMillis(),
            onTimeSelected = { startTime = it },
            onDismiss = { showTimePicker = false }
        )
    }
}