package com.sevban.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sevban.sync.util.SyncLogger
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val syncLogger: SyncLogger
) {
    
    companion object {
        private const val SYNC_INTERVAL_HOURS = 3L
        private const val SYNC_INTERVAL_MINS = 15L
        private const val SYNC_FLEX_INTERVAL_HOURS = 1L
        private const val SYNC_FLEX_INTERVAL_MINS = 5L
        private const val CLEANUP_INTERVAL_HOURS = 24L // Daily cleanup
        private const val CLEANUP_FLEX_INTERVAL_HOURS = 2L
    }

    private val workManager by lazy { WorkManager.getInstance(context) }
    
    /**
     * Initialize periodic weather sync with intelligent constraints
     */
    fun initializePeriodicSync() {
        try {
            syncLogger.logSyncManagerStart()
            
            // Schedule weather sync
            val syncConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresDeviceIdle(false) // Allow sync when device is not idle
                .build()
            
            val periodicSyncRequest = PeriodicWorkRequestBuilder<WeatherSyncWorker>(
                repeatInterval = SYNC_INTERVAL_MINS,
                repeatIntervalTimeUnit = TimeUnit.MINUTES,
                flexTimeInterval = SYNC_FLEX_INTERVAL_MINS,
                flexTimeIntervalUnit = TimeUnit.MINUTES
            )
                .setConstraints(syncConstraints)
                .addTag(WeatherSyncWorker.WORK_NAME)
                .build()
            
            workManager.enqueueUniquePeriodicWork(
                WeatherSyncWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP, // Keep existing work if already scheduled
                periodicSyncRequest
            )
            
            // Schedule data cleanup
            val cleanupConstraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiresDeviceIdle(true) // Run cleanup when device is idle
                .build()
            
            val periodicCleanupRequest = PeriodicWorkRequestBuilder<DataCleanupWorker>(
                repeatInterval = CLEANUP_INTERVAL_HOURS,
                repeatIntervalTimeUnit = TimeUnit.HOURS,
                flexTimeInterval = CLEANUP_FLEX_INTERVAL_HOURS,
                flexTimeIntervalUnit = TimeUnit.HOURS
            )
                .setConstraints(cleanupConstraints)
                .addTag(DataCleanupWorker.WORK_NAME)
                .build()
            
            workManager.enqueueUniquePeriodicWork(
                DataCleanupWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicCleanupRequest
            )
            
            syncLogger.logSyncManagerScheduled()
            
        } catch (e: Exception) {
            syncLogger.logSyncManagerError(e)
        }
    }
    
    /**
     * Cancel all scheduled sync work
     */
    fun cancelPeriodicSync() {
        workManager.cancelUniqueWork(WeatherSyncWorker.WORK_NAME)
        workManager.cancelUniqueWork(DataCleanupWorker.WORK_NAME)
    }
    
    /**
     * Force an immediate sync (useful for testing or user-initiated sync)
     */
    fun forceSync() {
        try {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            
            val immediateWorkRequest = androidx.work.OneTimeWorkRequestBuilder<WeatherSyncWorker>()
                .setConstraints(constraints)
                .addTag("immediate_sync")
                .build()
            
            workManager.enqueue(immediateWorkRequest)
            
        } catch (e: Exception) {
            syncLogger.logSyncManagerError(e)
        }
    }
    
    /**
     * Check if sync is currently running
     */
    fun isSyncRunning(): Boolean {
        return try {
            val workInfos = workManager.getWorkInfosForUniqueWork(WeatherSyncWorker.WORK_NAME).get()
            workInfos.any { it.state == androidx.work.WorkInfo.State.RUNNING }
        } catch (e: Exception) {
            syncLogger.logSyncManagerError(e)
            false
        }
    }
} 