package com.example.weather.remote

import com.example.weather.data.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("current")
    suspend fun getCurrentWeather(
        @Query("access_key") accessKey: String,
        @Query("query") query: String
    ): Response<WeatherResponse>

}