package com.sevban.common.di

import android.content.Context
import com.sevban.common.location.AndroidLocationObserver
import com.sevban.common.location.LocationObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    @Singleton
    fun provideLocationClient(@ApplicationContext context: Context): com.sevban.common.location.LocationClient =
        com.sevban.common.location.LocationClientImpl(context)

    @Provides
    @Singleton
    fun provideLocationObserver(@ApplicationContext context: Context): LocationObserver =
        AndroidLocationObserver(context)
}