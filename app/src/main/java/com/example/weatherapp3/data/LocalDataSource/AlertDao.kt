
package com.example.weatherapp3.data.LocalDataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.weatherapp3.data.models.Alert
import kotlinx.coroutines.flow.Flow
@Dao
interface AlertDao {
    @Insert
    suspend fun insert(alert: Alert): Long

    @Delete
    suspend fun delete(alert: Alert)

    @Update
    suspend fun update(alert: Alert)

    @Query("SELECT * FROM alerts ORDER BY date DESC")
    fun getAllAlerts(): Flow<List<Alert>>
}