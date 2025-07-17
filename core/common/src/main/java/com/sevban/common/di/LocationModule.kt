package com.sevban.common.di

import android.content.Context
import com.sevban.common.helper.LocaleHelper
import com.sevban.common.helper.WeatherLocalizationService
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
    fun provideLocationObserver(@ApplicationContext context: Context): LocationObserver =
        AndroidLocationObserver(context)

    @Provides
    @Singleton
    fun provideWeatherLocalizationService(@ApplicationContext context: Context): WeatherLocalizationService =
        WeatherLocalizationService(context)

    @Provides
    @Singleton
    fun provideLocaleHelper(@ApplicationContext context: Context): LocaleHelper =
        LocaleHelper(context)
}