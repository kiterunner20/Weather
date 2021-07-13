package com.example.weather.di

import com.example.weather.remote.WeatherRemoteServer
import com.example.weather.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 * Providing the repository,and scope will be singleton .
 * Will be valid from app start to end
 */

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository( remoteServer: WeatherRemoteServer): WeatherRepository {
        return WeatherRepository(remoteServer)
    }

}
