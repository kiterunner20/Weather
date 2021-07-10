package com.example.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weather.databinding.FragmentWeatherDataBinding

class WeatherFragment : Fragment() {

    private lateinit var binding: FragmentWeatherDataBinding


    companion object {

        fun getFragmentInstance(): WeatherFragment {
            return WeatherFragment()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWeatherDataBinding.inflate(inflater, container, false)
        return binding.root
    }

}