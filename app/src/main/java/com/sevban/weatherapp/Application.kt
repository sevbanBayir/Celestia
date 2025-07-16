package com.sevban.weatherapp

import android.app.Application
import com.sevban.sync.SyncManager
import com.sevban.sync.di.WorkManagerInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HiltApplication : Application() {
    
    @Inject
    lateinit var syncManager: SyncManager
    
    @Inject
    lateinit var workManagerInitializer: WorkManagerInitializer
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize WorkManager with Hilt
        workManagerInitializer.initialize()
        
        // Initialize background sync for weather data
        syncManager.initializePeriodicSync()
    }
}