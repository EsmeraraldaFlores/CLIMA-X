package com.example.staysunny.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.staysunny.R
import com.example.staysunny.core.LocationProvider
import com.example.staysunny.databinding.FragmentWeatherBinding
import com.example.staysunny.model.Weather
import com.example.staysunny.utils.FragmentCommunicator
import com.example.staysunny.viewModel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private val weatherVM by viewModels<WeatherViewModel>()
    private lateinit var uiBridge: FragmentCommunicator

    private val permissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            fetchLocation(locationCallback)
        }
    }

    private val locationCallback: (String?) -> Unit = { coords ->
        if (coords != null) {
            Log.d("WeatherFragment", "Ubicación obtenida: $coords")
            weatherVM.getWeatherDetail(coords)
        } else {
            Toast.makeText(requireContext(), "No fue posible obtener la ubicación.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        uiBridge = requireActivity() as FragmentCommunicator
        initializeUI()
        observeViewModel()
        return binding.root
    }

    private fun initializeUI() {
        if (hasLocationPermission()) {
            Log.d("WeatherFragment", "Permiso de ubicación ya otorgado.")
            fetchLocation(locationCallback)
        } else {
            Log.d("WeatherFragment", "Solicitando permisos de ubicación...")
            permissionRequestLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun fetchLocation(onResult: (String?) -> Unit) {
        if (!hasLocationPermission()) {
            Log.w("WeatherFragment", "Permisos de ubicación denegados.")
            onResult(null)
            return
        }

        lifecycleScope.launch {
            try {
                val loc = LocationProvider.getInstance(requireContext()).getCurrentLocation()
                loc?.let {
                    val coords = "${it.latitude},${it.longitude}"
                    Log.i("Weather", "Coords: $coords")
                    onResult(coords)
                } ?: run {
                    Log.e("Weather", "Ubicación nula.")
                    onResult(null)
                }
            } catch (e: Exception) {
                Log.e("Weather", "Error obteniendo ubicación: ${e.message}")
                onResult(null)
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        val ctx = requireContext()
        return ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun observeViewModel() {
        weatherVM.weatherInfo.observe(viewLifecycleOwner) { renderWeather(it) }
        weatherVM.loaderState.observe(viewLifecycleOwner) { uiBridge.showLoader(it) }
    }

    private fun renderWeather(data: Weather) = with(binding) {
        tvCity.text = data.location.name
        tvDate.text = data.location.localtime.split(" ")[1]
        tvCelsius.text = "${data.current.tempC} °C"
        tvGreeting.text = if (data.current.isDay == 1) "Good morning" else "Good night"
        tvSunriseInfo.text = "${data.location.localtime.split(" ")[1]} hrs"
        tvWindInfo.text = "${data.current.windMph} m/s"
        tvTempratureInfo.text = "${data.current.tempC} °C"

        Glide.with(this@WeatherFragment)
            .load("https:${data.current.condition.icon}")
            .placeholder(R.drawable.pending_24px)
            .error(R.drawable.sentiment_sad_24px)
            .into(ivWeather)
    }

    fun getUserCoordinates(): String = "19.32871829633027, -99.16549389549148"

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
