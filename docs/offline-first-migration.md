# Migrating from Online-First to Offline-First Architecture

## Overview

This document outlines the migration process from an online-first Android application to an offline-first architecture. The goal is to provide a seamless user experience where users see cached data immediately upon app launch, eliminating loading states except for the initial installation.

## Architecture Changes

### Before: Online-First Architecture
- Direct API calls from Repository to Network layer
- No local data persistence
- Loading states on every app launch
- No offline functionality

### After: Offline-First Architecture
- Database as single source of truth
- Network layer for synchronization
- Immediate data availability
- Graceful offline experience

## Implementation Steps

### 1. Database Module Setup

#### 1.1 Create Database Module
First, we created the `core/database` module with the following structure:

```
core/database/
├── src/main/java/com/sevban/database/
│   ├── model/
│   │   ├── WeatherEntity.kt
│   │   ├── ForecastEntity.kt
│   │   ├── ForecastWeatherEntity.kt
│   │   └── ForecastWithWeatherItems.kt
│   ├── mapper/
│   │   ├── WeatherEntityMapper.kt
│   │   ├── ForecastEntityMapper.kt
│   │   └── NetworkToEntityMapper.kt
│   ├── di/
│   │   └── DatabaseModule.kt
│   ├── CelestiaDatabase.kt
│   └── WeatherDao.kt
└── build.gradle.kts
```

#### 1.2 Build Configuration
Updated `build.gradle.kts` to include:
- Room database dependencies
- Kotlin annotation processing (kapt)
- Network and model module dependencies

### 2. Database Entities

#### 2.1 Entity Design Principles
All entities include offline-first metadata:
- `isSynced`: Boolean flag indicating if data is synchronized
- `lastUpdated`: Timestamp when data was last updated
- `syncedAt`: Timestamp when data was last synchronized with server

#### 2.2 Entity Structure

**WeatherEntity:**
```kotlin
@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val id: Int,
    // Weather data fields...
    val latitude: Double,
    val longitude: Double,
    val isSynced: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis(),
    val syncedAt: Long? = null
)
```

**ForecastEntity & ForecastWeatherEntity:**
- One-to-many relationship using Room's `@Relation`
- Foreign key constraints for data integrity
- Composite primary key using location coordinates

### 3. Data Access Layer (DAO)

#### 3.1 Query Patterns
The DAO implements several query patterns for offline-first:

**Reactive Queries:**
```kotlin
@Query("SELECT * FROM weather WHERE latitude = :lat AND longitude = :lng")
fun getWeatherByLocation(lat: Double, lng: Double): Flow<WeatherEntity?>
```

**Sync Management:**
```kotlin
@Query("SELECT * FROM weather WHERE isSynced = 0")
suspend fun getUnsyncedWeather(): List<WeatherEntity>
```

**Data Cleanup:**
```kotlin
@Query("DELETE FROM weather WHERE lastUpdated < :timestamp")
suspend fun deleteOldWeatherData(timestamp: Long)
```

### 4. Database Setup

#### 4.1 Room Configuration
```kotlin
@Database(
    entities = [WeatherEntity::class, ForecastEntity::class, ForecastWeatherEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CelestiaDatabase : RoomDatabase()
```

#### 4.2 Dependency Injection
Hilt module provides:
- Database instance (Singleton)
- DAO instances
- Proper lifecycle management

### 5. Mapping Strategies

#### 5.1 Three-Layer Mapping
1. **Network to Entity**: Save API responses to database (located in data module)
2. **Entity to Domain**: Convert database entities to business models (located in data module)
3. **Domain to UI**: Existing domain to UI mapping (unchanged)

#### 5.2 Clean Architecture Separation
- **Database Module**: Independent, only depends on model module, deals only with entities
- **Network Module**: Independent, only handles API calls, deals only with DTOs
- **Data Module**: Orchestration layer, depends on both database and network modules, contains all mappers and handles conversions

#### 5.2 Mapping Examples

**Network to Entity:**
```kotlin
fun WeatherDTO.toEntity(latitude: Double, longitude: Double): WeatherEntity {
    return WeatherEntity(
        // Map fields...
        isSynced = true,
        lastUpdated = System.currentTimeMillis(),
        syncedAt = System.currentTimeMillis()
    )
}
```

**Entity to Domain:**
```kotlin
fun WeatherEntity.toDomain() = Weather(
    id = this.id,
    description = this.description,
    // Map other fields...
)
```

### 6. Current Implementation Status

#### 6.1 Repository Pattern (Simplified)
For now, the repository maintains the original online-first pattern:
```kotlin
class WeatherForecastRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val dispatcherProvider: DispatcherProvider
) : WeatherForecastRepository {
    
    override fun getLocationWeather(lat: String, long: String): Flow<Weather> =
        weatherRemoteDataSource.getLocationWeather(lat, long)
            .asRestApiCall(WeatherDTO::toWeather)
            .catch {
                if (it is UnknownHostException) {
                    throw Failure(ErrorType.CONNECTIVITY_ERROR)
                } else throw it
            }
            .flowOn(dispatcherProvider.ioDispatcher)
}
```

#### 6.2 Ready for WorkManager Integration
The infrastructure is in place for offline-first implementation:
- ✅ Database entities and DAOs
- ✅ Local data source abstraction (returns entities only)
- ✅ Network to entity mappers
- ✅ Entity to domain mappers
- ✅ Clean layer separation (database layer only deals with entities)
- 🔄 **Next**: WorkManager sync implementation

#### 6.3 Clean Architecture Achieved
```
Database Layer:     Entity ←→ Entity
Network Layer:      DTO ←→ DTO  
Data Layer:         Entity ←→ DTO ←→ Domain Model
```

The data layer will handle all conversions:
- Network DTOs → Database Entities (for caching)
- Database Entities → Domain Models (for business logic)
- Network DTOs → Domain Models (for direct usage)

## Next Steps

### 7. Testing Strategy
- Unit tests for mappers
- Integration tests for database operations
- End-to-end tests for offline scenarios
- Performance tests for large datasets

### 8. Advanced Features (Optional)
- **Periodic Background Sync**: Use WorkManager for scheduled updates
- **Conflict Resolution**: Handle data conflicts when user makes changes offline
- **Data Expiry**: Implement TTL for different types of data
- **Selective Sync**: Allow users to choose what data to sync
- **Sync Status UI**: Show sync progress and status to users

## Key Benefits

1. **Instant App Launch**: Users see cached data immediately
2. **Offline Functionality**: App works without internet connection
3. **Better UX**: No loading spinners on subsequent launches
4. **Data Persistence**: User data survives app restarts
5. **Robust Architecture**: Graceful handling of network issues

## Technical Considerations

### Data Freshness
- Implement TTL (Time To Live) for cached data
- Background sync when network is available
- Visual indicators for stale data

### Storage Management
- Periodic cleanup of old data
- Implement data size limits
- Consider user storage constraints

### Performance
- Use Room's reactive queries with Flow
- Implement proper indexing
- Consider pagination for large datasets

### Error Handling
- Graceful degradation when database is unavailable
- Backup strategies for critical data
- User feedback for sync failures

## Conclusion

This migration transforms the app from a network-dependent application to a robust offline-first experience. The database becomes the authoritative source of truth, while the network layer handles synchronization in the background. This approach significantly improves user experience by eliminating loading states and providing immediate access to content.

The implementation maintains clean architecture principles while adding the necessary infrastructure for offline functionality. The modular approach allows for incremental migration and testing of individual components. 