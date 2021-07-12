package com.example.weather.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.weather.repository.WeatherRepository

class WeatherViewModel @ViewModelInject
constructor(private val repository: WeatherRepository) : ViewModel() {

}