package com.sevban.sync.di

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkManagerInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val workerFactory: HiltWorkerFactory
) {
    
    fun initialize() {
        val configuration = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        
        WorkManager.initialize(context, configuration)
    }
} 