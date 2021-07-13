package com.example.weather.repository

import com.example.weather.data.WeatherResponse
import com.example.weather.remote.WeatherRemoteServer
import retrofit2.Response
import javax.inject.Inject

/**
 * Repo design pattern implemented to separate Network and DB calls
 */
class WeatherRepository @Inject constructor(private val remoteServer: WeatherRemoteServer) {

    suspend fun getWeatherData(location: String): Response<WeatherResponse> {
        return remoteServer.getWeatherData(location)
    }
}