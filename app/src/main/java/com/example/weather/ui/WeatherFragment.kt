package com.example.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.weather.base.BaseFragment
import com.example.weather.databinding.FragmentWeatherDataBinding
import com.example.weather.utility.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.weather.utility.LocationUtility
import com.example.weather.vm.WeatherViewModel
import com.example.weather.vm.viewstate.WeatherDataState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


@AndroidEntryPoint
class WeatherFragment : BaseFragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: FragmentWeatherDataBinding
    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var selectedLanguage = ""


    companion object {

        fun getFragmentInstance(): WeatherFragment {
            return WeatherFragment()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWeatherDataBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun initDependencies() {
        fusedLocationClient = context?.let {
            LocationServices.getFusedLocationProviderClient(it)
        }!!


    }


    override fun onReady() {
        weatherViewModel.getWeatherData().observe(this, {
            when (it) {
                WeatherDataState.PROGRESS -> showProgress()
                WeatherDataState.FAILED -> showError(it.error.toString())

                WeatherDataState.SUCCESS -> {
                    val weatherData = it.data?.body()
                    binding.tvTemparature.text = "${weatherData?.current?.temperature} \u2103"
                    binding.tvHumidityData.text = weatherData?.current?.humidity.toString()
                    binding.tvWindData.text = "${weatherData?.current?.windSpeed.toString()} Km/h"
                    binding.tvWeatherStatus.text =
                        weatherData?.current?.weatherDescriptions?.get(0).toString()

                    Glide.with(context as Context)
                        .load(weatherData?.current?.weatherIcons?.get(0))
                        .override(300, 200)
                        .circleCrop()
                        .into(binding.imWeatherIcons);

                }
            }
        })


        binding.themeSwitch.setOnClickListener {
            when (selectedLanguage) {
                "" -> {
                    selectedLanguage = "hi"
                    switchLanguage(selectedLanguage)
                }
                "hi" -> {
                    selectedLanguage = "en"
                    switchLanguage(selectedLanguage)
                }
                "en" -> {
                    selectedLanguage = "hi"
                    switchLanguage(selectedLanguage)
                }
            }
            switchTheme()

        }

    }

    @SuppressLint("MissingPermission")
    override fun fetchLocation() {
        if (askForPermissions()) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val city = findCityMapped(it)
                    withContext(Dispatchers.Main) {
                        setCityAndFetchWeather(city)
                    }
                }

            }
        }

    }

    private fun setCityAndFetchWeather(city: String) {
        binding.tvCurrentLocation.text = city
        weatherViewModel.fetchWeatherData(city)
    }

    fun switchTheme() {
        if (AppCompatDelegate.getDefaultNightMode() ==
            AppCompatDelegate.MODE_NIGHT_YES
        ) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

    }

    fun switchLanguage(selectedLan: String) {
        activity as MainActivity setLangCode (selectedLan)
    }

    private fun showProgress() {

    }

    private fun showError(error: String) {}

    private fun findCityMapped(location: Location): String {
        val addresses = try {
            Geocoder(context, Locale.getDefault()).getFromLocation(
                location.latitude,
                location.longitude,
                1
            )
        } catch (e: Exception) {
            return ""
        }

        return addresses[0].locality
    }


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        fetchLocation()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            askForPermissions()
        }
    }

    private fun askForPermissions(): Boolean {
        if (LocationUtility.hasLocationPermissions(context)) {
            return true
        }

        EasyPermissions.requestPermissions(
            this,
            "You need to accept location permission to use this app",
            REQUEST_CODE_LOCATION_PERMISSION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return false
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}