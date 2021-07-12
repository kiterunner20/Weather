package com.example.weather.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.WeatherResponse
import com.example.weather.repository.WeatherRepository
import com.example.weather.vm.viewstate.WeatherDataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class WeatherViewModel @ViewModelInject
constructor(private val repository: WeatherRepository) : ViewModel() {


    private var weatherDataState: MutableLiveData<WeatherDataState> = MutableLiveData()


    fun getWeatherData(): LiveData<WeatherDataState> {
        return weatherDataState
    }

    fun fetchWeatherData(location: String) {
        showProgress()
        viewModelScope.launch {
            withData(repository.getWeatherData(location))
        }

    }

    private suspend fun withData(weatherData: Response<WeatherResponse>) {
        withContext(Dispatchers.Main) {
            if (weatherData.isSuccessful) {
                WeatherDataState.SUCCESS.data = weatherData
                weatherDataState.postValue(WeatherDataState.SUCCESS)
            } else {
                onFailure(weatherData.errorBody().toString())
            }
        }
    }

    private fun showProgress() {
        weatherDataState.postValue(WeatherDataState.PROGRESS)
    }

    private fun onFailure(error: String) {
        WeatherDataState.FAILED.error = error
        weatherDataState.postValue(WeatherDataState.FAILED)
    }


}