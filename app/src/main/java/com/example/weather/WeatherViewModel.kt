package com.example.weather

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel

class WeatherViewModel @ViewModelInject
constructor(private val repository: WeatherRepository) : ViewModel() {

}