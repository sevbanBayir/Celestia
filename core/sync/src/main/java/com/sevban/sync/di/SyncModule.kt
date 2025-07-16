package com.sevban.sync.di

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import com.sevban.sync.SyncManager
import com.sevban.sync.util.SyncLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SyncModule {
    
    @Provides
    @Singleton
    fun provideSyncLogger(): SyncLogger {
        return SyncLogger()
    }
    
    @Provides
    @Singleton
    fun provideSyncManager(
        @ApplicationContext context: Context,
        syncLogger: SyncLogger
    ): SyncManager {
        return SyncManager(context, syncLogger)
    }
    
    @Provides
    @Singleton
    fun provideWorkManagerInitializer(
        @ApplicationContext context: Context,
        workerFactory: HiltWorkerFactory
    ): WorkManagerInitializer {
        return WorkManagerInitializer(context, workerFactory)
    }
} 