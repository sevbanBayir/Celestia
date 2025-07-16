package com.sevban.sync.util

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncLogger @Inject constructor() {
    
    companion object {
        private const val TAG = "WeatherSync"
        private const val DEBUG = true // Set to false for production
    }
    
    fun logSyncStart() {
        if (DEBUG) Log.d(TAG, "Starting background weather sync")
    }
    
    fun logSyncComplete() {
        if (DEBUG) Log.d(TAG, "Background weather sync completed successfully")
    }
    
    fun logSyncLocation(latitude: String, longitude: String) {
        if (DEBUG) Log.d(TAG, "Syncing weather data for location: $latitude, $longitude")
    }
    
    fun logWeatherSyncSuccess() {
        if (DEBUG) Log.d(TAG, "Weather data sync successful")
    }
    
    fun logForecastSyncSuccess() {
        if (DEBUG) Log.d(TAG, "Forecast data sync successful")
    }
    
    fun logWeatherSyncError(error: Exception) {
        Log.w(TAG, "Weather sync failed: ${error.message}", error)
    }
    
    fun logForecastSyncError(error: Exception) {
        Log.w(TAG, "Forecast sync failed: ${error.message}", error)
    }
    
    fun logLocationPermissionError() {
        Log.w(TAG, "Location permission denied - cannot sync weather data")
    }
    
    fun logLocationError(error: Exception) {
        Log.w(TAG, "Location error: ${error.message}", error)
    }
    
    fun logLocationUnavailable() {
        Log.w(TAG, "Location unavailable - cannot sync weather data")
    }
    
    fun logGeneralSyncError(error: Exception) {
        Log.e(TAG, "General sync error: ${error.message}", error)
    }
    
    fun logSyncManagerStart() {
        if (DEBUG) Log.d(TAG, "SyncManager: Starting periodic sync setup")
    }
    
    fun logSyncManagerScheduled() {
        if (DEBUG) Log.d(TAG, "SyncManager: Periodic sync scheduled successfully")
    }
    
    fun logSyncManagerError(error: Exception) {
        Log.e(TAG, "SyncManager error: ${error.message}", error)
    }
} 