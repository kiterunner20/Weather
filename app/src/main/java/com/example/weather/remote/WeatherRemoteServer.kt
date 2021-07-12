package com.example.weather.remote

import com.example.weather.data.WeatherResponse
import com.example.weather.utility.Constants.API_KEY
import retrofit2.Response
import javax.inject.Inject

class WeatherRemoteServer @Inject constructor(private val weatherService: WeatherService) {


    suspend fun getWeatherData(cityName: String): Response<WeatherResponse> {
        return weatherService.getCurrentWeather(API_KEY,cityName)
    }

}