package com.sevban.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sevban.common.helper.DispatcherProvider
import com.sevban.database.source.WeatherLocalDataSource
import com.sevban.sync.util.SyncLogger
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.withContext

@HiltWorker
class DataCleanupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val dispatcherProvider: DispatcherProvider,
    private val syncLogger: SyncLogger
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val WORK_NAME = "data_cleanup_work"
        
        // Data older than 7 days will be cleaned up
        private const val DATA_RETENTION_DAYS = 7L
        private const val MILLIS_PER_DAY = 24 * 60 * 60 * 1000L
    }

    override suspend fun doWork(): Result = withContext(dispatcherProvider.ioDispatcher) {
        try {
            syncLogger.logSyncStart()
            
            val cutoffTimestamp = System.currentTimeMillis() - (DATA_RETENTION_DAYS * MILLIS_PER_DAY)
            
            // Clean up old weather data
            weatherLocalDataSource.deleteOldWeatherData(cutoffTimestamp)
            
            // Clean up old forecast data
            weatherLocalDataSource.deleteOldForecastData(cutoffTimestamp)
            
            syncLogger.logSyncComplete()
            Result.success()
            
        } catch (e: Exception) {
            syncLogger.logGeneralSyncError(e)
            Result.retry()
        }
    }
} 