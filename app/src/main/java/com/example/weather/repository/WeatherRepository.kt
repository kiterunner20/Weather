package com.example.weather.repository

import com.example.weather.remote.WeatherRemoteServer
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val remoteServer: WeatherRemoteServer) {


}