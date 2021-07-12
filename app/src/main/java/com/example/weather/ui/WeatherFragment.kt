package com.example.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.weather.vm.WeatherViewModel
import com.example.weather.base.BaseFragment
import com.example.weather.utility.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.weather.databinding.FragmentWeatherDataBinding
import com.example.weather.utility.LocationUtility
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
    }


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