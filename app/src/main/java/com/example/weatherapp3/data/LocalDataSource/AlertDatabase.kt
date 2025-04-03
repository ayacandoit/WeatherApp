package com.example.weatherapp3.data.LocalDataSource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp3.data.models.Alert
import com.example.weatherapp3.data.models.FavoriteLocation

@Database(entities = [Alert::class], version = 1)

abstract class WeatherAlertDatabase : RoomDatabase() {
    abstract fun weatherAlertDao(): AlertDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherAlertDatabase? = null

        fun getDatabase(context: Context): WeatherAlertDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherAlertDatabase::class.java,
                    "weather_alerts_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}