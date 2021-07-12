package com.example.weather.vm.viewstate

open class BaseViewState<T> {

    var data: T? = null
    var error: String? = null
    var currentState: State? = null


    enum class State(var value: Int) {
        LOADING(0), SUCCESS(1), FAILED(-1);

    }
}