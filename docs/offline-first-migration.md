# Migrating from Online-First to Offline-First Architecture

## Overview

This document outlines the migration process from an online-first Android application to an offline-first architecture. The goal is to provide a seamless user experience where users see cached data immediately upon app launch, eliminating loading states and providing graceful offline functionality.

## Architecture Changes

### Before: Online-First Architecture
- Direct API calls from Repository to Network layer
- No local data persistence
- Loading states on every app launch
- No offline functionality
- Exact location matching causing cache misses

### After: Offline-First Architecture
- Database as single source of truth
- Network layer for synchronization
- Immediate data availability with reactive updates
- Graceful offline experience
- Proximity-based location matching for GPS variations

## Implementation Steps

### 1. Database Module Setup

#### 1.1 Create Database Module
The `core/database` module implements the local storage layer:

```
core/database/
├── src/main/java/com/sevban/database/
│   ├── model/
│   │   ├── WeatherEntity.kt
│   │   ├── ForecastEntity.kt
│   │   ├── ForecastWeatherEntity.kt
│   │   └── ForecastWithWeatherItems.kt
│   ├── source/
│   │   ├── WeatherLocalDataSource.kt
│   │   └── WeatherLocalDataSourceImpl.kt
│   ├── di/
│   │   └── DatabaseModule.kt
│   ├── CelestiaWeatherDatabase.kt
│   └── WeatherDao.kt
└── build.gradle.kts
```

#### 1.2 Build Configuration
Updated `build.gradle.kts` to include:
- Room database dependencies
- Kotlin Symbol Processing (KSP)
- Necessary module dependencies

```kotlin
dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    
    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}
```

### 2. Entity Design with Sync Metadata

#### 2.1 WeatherEntity
```kotlin
@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val id: Int,
    val description: String?,
    val icon: String?,
    val cityName: String?,
    val feelsLike: Double?,
    val temp: Double?,
    val tempMax: Double?,
    val tempMin: Double?,
    val humidity: Int?,
    val pressure: Int?,
    val windSpeed: Double?,
    val rainVolume1h: Double?,
    val visibility: Int?,
    val latitude: Double,
    val longitude: Double,
    val isSynced: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis(),
    val syncedAt: Long? = null
)
```

#### 2.2 ForecastEntity with Location-Tolerant IDs
```kotlin
@Entity(tableName = "forecast")
data class ForecastEntity(
    @PrimaryKey val id: String, // Generated using rounded coordinates
    val cod: String?,
    val city: String?,
    val cnt: Int?,
    val message: Int?,
    val latitude: Double,
    val longitude: Double,
    val isSynced: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis(),
    val syncedAt: Long? = null
)
```

### 3. Proximity-Based Location Matching

#### 3.1 The Problem
GPS coordinates naturally vary slightly (e.g., `40.7128` vs `40.7129`), causing exact database matches to fail and triggering unnecessary network requests.

#### 3.2 The Solution
Implemented proximity-based queries with 0.01° tolerance (~1.1km):

```kotlin
@Dao
interface WeatherDao {
    companion object {
        const val LOCATION_TOLERANCE_DEGREES = 0.01
    }
    
    @Query("""
        SELECT * FROM weather 
        WHERE ABS(latitude - :lat) <= :tolerance 
        AND ABS(longitude - :lng) <= :tolerance
        ORDER BY ABS(latitude - :lat) + ABS(longitude - :lng) ASC
        LIMIT 1
    """)
    fun getWeatherByLocation(
        lat: Double, 
        lng: Double, 
        tolerance: Double = LOCATION_TOLERANCE_DEGREES
    ): Flow<WeatherEntity?>
}
```

#### 3.3 Location-Tolerant Forecast IDs
```kotlin
private fun generateLocationId(latitude: Double, longitude: Double): String {
    val roundedLat = round(latitude * 100) / 100  // Round to 2 decimal places
    val roundedLng = round(longitude * 100) / 100
    return "${roundedLat}_${roundedLng}"
}
```

### 4. Data Layer Architecture

#### 4.1 Clean Architecture Separation
- **Database Module**: Only handles entities, no domain model knowledge
- **Network Module**: Only handles DTOs, no entity knowledge  
- **Data Module**: Orchestration layer with all mappers

#### 4.2 Mapping Strategy
Three-layer mapping approach:
1. **Network to Entity**: Save API responses to database
2. **Entity to Domain**: Convert database entities to business models
3. **Domain to UI**: Existing domain to UI mapping

### 5. Repository Implementation

#### 5.1 Reactive Offline-First Pattern
```kotlin
@Singleton
class WeatherForecastRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val dispatcherProvider: DispatcherProvider,
    @RepositoryScope private val repositoryScope: CoroutineScope
) : WeatherForecastRepository {

    override fun getLocationWeather(lat: String, long: String): Flow<Weather> {
        val latitude = lat.toDouble()
        val longitude = long.toDouble()
        
        return weatherLocalDataSource.getWeatherByLocation(latitude, longitude)
            .mapNotNull { entity ->
                val weather = entity?.toDomain()
                if (weather == null) {
                    // Trigger network fetch when no data available
                    fetchWeatherFromNetwork(latitude, longitude)
                }
                weather // Return null until data is fetched and saved
            }
            .flowOn(dispatcherProvider.ioDispatcher)
    }
}
```

#### 5.2 Key Features
- **Immediate Response**: Returns database Flow immediately
- **Reactive Updates**: UI automatically updates when network fetch completes
- **Graceful Degradation**: Filters out null values with `mapNotNull`
- **Background Fetching**: Network calls happen asynchronously

### 6. Network Integration

#### 6.1 Enhanced Flow Extension
Created `asRestApiCallWithDto()` extension for better network handling:

```kotlin
fun <T> Flow<T>.asRestApiCallWithDto(): Flow<T> = this
    .catch { exception ->
        when (exception) {
            is UnknownHostException -> throw Failure(ErrorType.CONNECTIVITY_ERROR)
            else -> throw exception
        }
    }
    .map { dto -> dto } // Provides original DTO for processing
```

#### 6.2 Network Error Handling
```kotlin
private suspend fun fetchWeatherFromNetwork(latitude: Double, longitude: Double) {
    try {
        weatherRemoteDataSource.getLocationWeather(latitude.toString(), longitude.toString())
            .asRestApiCallWithDto()
            .catch { exception ->
                if (exception is UnknownHostException) {
                    return@catch // Expected in offline mode
                } else {
                    throw exception
                }
            }
            .collect { weatherDTO ->
                val weatherEntity = weatherDTO.toEntity(latitude, longitude)
                weatherLocalDataSource.insertWeather(weatherEntity)
            }
    } catch (e: Exception) {
        // Network failures are non-critical in offline-first mode
    }
}
```

### 7. Dependency Injection

#### 7.1 Coroutine Scope Management
```kotlin
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RepositoryScope

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatcherModule {
    @Binds
    abstract fun provideDispatcherProvider(impl: StandardDispatcherProvider): DispatcherProvider

    companion object {
        @Provides
        @Singleton
        @RepositoryScope
        fun provideRepositoryScope(dispatcherProvider: DispatcherProvider): CoroutineScope {
            return CoroutineScope(SupervisorJob() + dispatcherProvider.ioDispatcher)
        }
    }
}
```

### 8. Error Handling

#### 8.1 Error Types
```kotlin
enum class ErrorType {
    CONNECTIVITY_ERROR,    // Network unavailable
    NO_DATA_AVAILABLE,     // No cached data, suggest refresh
    // ... other errors
}
```

#### 8.2 User-Friendly Messages
- **English**: `"No data available. Pull to refresh."`
- **Turkish**: `"Kullanılabilir veri yok. Yenilemek için kaydırın."`

### 9. Force Refresh Capability

#### 9.1 User-Initiated Refresh
```kotlin
interface WeatherForecastRepository {
    suspend fun forceRefreshWeather(lat: String, long: String)
    suspend fun forceRefreshForecast(lat: String, long: String)
}
```

#### 9.2 Implementation
```kotlin
override suspend fun forceRefreshWeather(lat: String, long: String) {
    val latitude = lat.toDouble()
    val longitude = long.toDouble()
    
    try {
        fetchWeatherFromNetwork(latitude, longitude)
    } catch (e: UnknownHostException) {
        throw Failure(ErrorType.CONNECTIVITY_ERROR)
    }
}
```

## Benefits Achieved

### 1. Performance
- ✅ **Eliminated Loading States**: Data appears instantly from database
- ✅ **Reduced Network Calls**: Proximity matching reuses nearby data
- ✅ **Reactive Updates**: UI automatically reflects database changes

### 2. User Experience
- ✅ **Instant App Launch**: No waiting for network requests
- ✅ **Offline Functionality**: Full app experience without internet
- ✅ **GPS Tolerance**: Handles natural coordinate variations

### 3. Architecture
- ✅ **Clean Separation**: Database, Network, and Data layers are independent
- ✅ **Testable**: All dependencies are injected
- ✅ **Maintainable**: Clear responsibilities and reactive patterns

### 4. Scalability
- ✅ **Location-Aware**: Proximity search scales with user movement
- ✅ **Configurable**: Tolerance and thresholds can be adjusted
- ✅ **Future-Ready**: Foundation for sync strategies and conflict resolution

## Technical Decisions

### 1. Proximity Search vs Exact Matching
**Decision**: Implemented proximity-based location matching with 0.01° tolerance
**Reasoning**: GPS coordinates naturally vary, exact matching causes cache misses

### 2. Direct Network Fetch vs Background Refresh
**Decision**: Direct network fetch when no data available
**Reasoning**: Simpler implementation, reactive updates, immediate feedback

### 3. mapNotNull vs map with Error Handling
**Decision**: Use `mapNotNull` to filter out null values
**Reasoning**: Prevents crashes, allows UI to wait for data reactively

### 4. Location-Tolerant IDs
**Decision**: Round coordinates to 2 decimal places for forecast IDs
**Reasoning**: Reduces duplicate entries for nearby locations

## Migration Checklist

- [x] Database module with Room implementation
- [x] Entity design with sync metadata
- [x] Proximity-based location queries
- [x] Location-tolerant forecast IDs
- [x] Data layer with mappers
- [x] Repository with reactive pattern
- [x] Network integration with error handling
- [x] Dependency injection setup
- [x] Force refresh capability
- [x] Comprehensive error handling
- [x] Localization support

## Future Enhancements

1. **Conflict Resolution**: Handle simultaneous updates from multiple sources
2. **Intelligent Sync**: Sync only when necessary based on user behavior
3. **Cache Invalidation**: Smart cache cleanup based on usage patterns
4. **Performance Monitoring**: Track cache hit rates and network usage
5. **Advanced Location**: Consider more sophisticated location matching algorithms

## Conclusion

The offline-first migration successfully transforms the app from a network-dependent experience to a responsive, offline-capable application. The implementation provides immediate data availability, graceful offline functionality, and handles real-world GPS coordinate variations through proximity-based matching. 