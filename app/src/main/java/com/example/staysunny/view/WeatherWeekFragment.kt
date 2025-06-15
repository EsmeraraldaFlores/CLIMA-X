package com.example.staysunny.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.staysunny.adapter.WeatherWeekAdapter
import com.example.staysunny.databinding.FragmentWeatherWeekBinding
import com.example.staysunny.viewModel.WeatherWeekViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherWeekFragment : Fragment() {

    private var _binding: FragmentWeatherWeekBinding? = null
    private val binding get() = _binding!!
    private val weekVM by viewModels<WeatherWeekViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWeatherWeekBinding.inflate(inflater, container, false)
        initRecycler()
        observeViewModel()
        requestWeeklyForecast()
        return binding.root
    }

    private fun initRecycler() = with(binding.rvWeekForecast) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = WeatherWeekAdapter(emptyList()) // Adapter inicial con lista vacía
    }

    private fun observeViewModel() {
        weekVM.weatherForecast.observe(viewLifecycleOwner) { forecast ->
            if (forecast != null) {
                val dates = forecast.forecastDays.map { it.date }
                Log.d("WeatherWeek", "Fechas recibidas: $dates")
                (binding.rvWeekForecast.adapter as? WeatherWeekAdapter)?.updateData(forecast.forecastDays)
            } else {
                Log.e("WeatherWeek", "No se recibieron datos del pronóstico.")
            }
        }

        weekVM.loaderState.observe(viewLifecycleOwner) { loading ->
            binding.pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun requestWeeklyForecast() {
        val sampleCoords = "19.32871829633027,-99.16549389549148"
        weekVM.getWeatherWeekDetail(sampleCoords)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

