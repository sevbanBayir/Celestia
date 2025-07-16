package com.sevban.common.di

import com.sevban.common.helper.DispatcherProvider
import com.sevban.common.helper.StandardDispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

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