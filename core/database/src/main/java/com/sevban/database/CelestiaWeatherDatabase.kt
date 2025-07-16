package com.sevban.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.sevban.database.model.ForecastEntity
import com.sevban.database.model.ForecastWeatherEntity
import com.sevban.database.model.WeatherEntity

@Database(
    entities = [
        WeatherEntity::class,
        ForecastEntity::class,
        ForecastWeatherEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CelestiaWeatherDatabase : RoomDatabase() {
    
    abstract fun weatherDao(): WeatherDao
    
    companion object Companion {
        const val DATABASE_NAME = "celestia_database"
        
        fun buildDatabase(context: Context): CelestiaWeatherDatabase {
            return Room.databaseBuilder(
                context,
                CelestiaWeatherDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}

