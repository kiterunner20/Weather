package com.example.weather.vm.viewstate

import com.example.weather.data.WeatherResponse
import retrofit2.Response

class WeatherDataState(data: Response<WeatherResponse>?, error: String, state: State) :
    BaseViewState<Response<WeatherResponse>>() {

    init {
        this.data = data
        this.currentState = state
        this.error = error
    }

    companion object {
        val SUCCESS = WeatherDataState(null, "", State.SUCCESS)
        val FAILED = WeatherDataState(null, "", State.FAILED)
        val PROGRESS = WeatherDataState(null, "", State.LOADING)

    }

}