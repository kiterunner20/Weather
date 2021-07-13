package com.example.weather.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.weather.R
import com.example.weather.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(R.id.container, WeatherFragment.getFragmentInstance())
            .commit()


    }

    infix fun setLangCode(code: String?) {
        val locale = Locale(code)
        val config = baseContext.resources.configuration
        config.locale = locale
        try {
            baseContext.resources
                .updateConfiguration(config, baseContext.resources.displayMetrics)
        } catch (e: Exception) {
            e.toString()
        }

    }


}