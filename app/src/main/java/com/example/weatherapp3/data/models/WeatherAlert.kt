

package com.example.weatherapp3.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.identity.util.UUID
import java.time.LocalDate
import java.time.LocalTime


@Entity(tableName = "alerts")
data class Alert(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val startTime: Long,
    val endTime: Long,
    val location: String,
    val lon:Double,
    val lit:Double,


    val workId: String? = null
)