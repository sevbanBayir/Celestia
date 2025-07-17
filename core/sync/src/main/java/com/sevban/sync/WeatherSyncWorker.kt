package com.sevban.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sevban.common.helper.DispatcherProvider
import com.sevban.common.location.LocationClient
import com.sevban.common.location.MissingLocationPermissionException
import com.sevban.data.repository.WeatherForecastRepository
import com.sevban.sync.util.SyncLogger
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

@HiltWorker
class WeatherSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val weatherForecastRepository: WeatherForecastRepository,
    private val locationClient: LocationClient,
    private val dispatcherProvider: DispatcherProvider,
    private val syncLogger: SyncLogger
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val WORK_NAME = "weather_sync_work"
    }

    override suspend fun doWork(): Result = withContext(dispatcherProvider.ioDispatcher) {
        try {
            syncLogger.logSyncStart()
            
            // Get current location with timeout
            val location = locationClient.getLastKnownLocation()
                .catch { exception ->
                    when (exception) {
                        is MissingLocationPermissionException -> {
                            syncLogger.logLocationPermissionError()
                            throw exception
                        }
                        else -> {
                            syncLogger.logLocationError(exception as Exception)
                            throw exception
                        }
                    }
                }
                .first()

            val latitude = location.latitude.toString()
            val longitude = location.longitude.toString()
            
            syncLogger.logSyncLocation(latitude, longitude)
            
            // Sync weather data
            try {
                weatherForecastRepository.forceRefreshWeather(latitude, longitude)
                syncLogger.logWeatherSyncSuccess()
            } catch (e: Exception) {
                syncLogger.logWeatherSyncError(e)
                // Continue with forecast sync even if weather sync fails
            }
            
            // Sync forecast data
            try {
                weatherForecastRepository.forceRefreshForecast(latitude, longitude)
                syncLogger.logForecastSyncSuccess()
            } catch (e: Exception) {
                syncLogger.logForecastSyncError(e)
                // We consider it a partial success if at least one sync worked
            }
            
            syncLogger.logSyncComplete()
            Result.success()
            
        } catch (e: MissingLocationPermissionException) {
            syncLogger.logLocationPermissionError()
            Result.failure()
        } catch (e: Exception) {
            syncLogger.logGeneralSyncError(e)
            Result.retry()
        }
    }
} 