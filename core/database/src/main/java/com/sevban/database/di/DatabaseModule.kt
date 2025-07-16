package com.sevban.database.di

import android.content.Context
import com.sevban.database.CelestiaWeatherDatabase
import com.sevban.database.WeatherDao
import com.sevban.database.source.WeatherLocalDataSource
import com.sevban.database.source.WeatherLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {
    
    @Binds
    abstract fun bindWeatherLocalDataSource(
        weatherLocalDataSourceImpl: WeatherLocalDataSourceImpl
    ): WeatherLocalDataSource
    
    companion object {
        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context
        ): CelestiaWeatherDatabase {
            return CelestiaWeatherDatabase.buildDatabase(context)
        }
        
        @Provides
        fun provideWeatherDao(database: CelestiaWeatherDatabase): WeatherDao {
            return database.weatherDao()
        }
    }
} 