package com.example.weather.base

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initDependencies()
        onReady()
        fetchLocation()
        super.onActivityCreated(savedInstanceState)
    }

    abstract fun initDependencies()

    abstract fun onReady()

    abstract fun fetchLocation()


}