package com.example.weather

import javax.inject.Inject

class WeatherRepository @Inject constructor(private val remoteServer: WeatherRemoteServer) {


}