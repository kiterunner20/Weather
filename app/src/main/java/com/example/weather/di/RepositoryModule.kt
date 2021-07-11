package com.example.weather.di

import com.example.weather.WeatherRemoteServer
import com.example.weather.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository( remoteServer: WeatherRemoteServer): WeatherRepository {
        return WeatherRepository(remoteServer)
    }

}
