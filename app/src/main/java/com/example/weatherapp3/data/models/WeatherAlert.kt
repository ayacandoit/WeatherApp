

package com.example.weatherapp3.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.identity.util.UUID
import java.time.LocalDate
import java.time.LocalTime


@Entity(tableName = "alerts")
data class Alert(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,          // Date in milliseconds
    val startTime: Long,     // Start time in milliseconds
    val endTime: Long,       // End time in milliseconds
    val location: String,
    val workId: String? = null  // WorkManager UUID
)