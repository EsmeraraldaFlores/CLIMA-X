package com.example.staysunny.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.staysunny.model.Weather.Forecast
import com.example.staysunny.network.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WeatherWeekViewModel @Inject constructor(
    private val weatherRepo: WeatherRepository
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean> get() = _loading

    private val _forecastData = MutableLiveData<Forecast>()
    val weatherForecast: LiveData<Forecast> get() = _forecastData

    fun getWeatherWeekDetail(coords: String) {
        _loading.value = true

        viewModelScope.launch {
            try {
                val result = weatherRepo.getWeatherDetail(coords)
                _loading.value = false

                result?.forecast?.forecastDays?.let { rawDays ->
                    Log.d("WeatherWeekVM", "Fechas crudas: ${rawDays.map { it.date }}")

                    val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val sortedDays = rawDays.sortedBy {
                        try {
                            parser.parse(it.date)
                        } catch (ex: Exception) {
                            Log.w("WeatherWeekVM", "Fecha inválida: ${it.date}")
                            parser.parse(parser.format(Date()))
                        }
                    }

                    val calendar = Calendar.getInstance()
                    val mondayIndex = sortedDays.indexOfFirst {
                        calendar.time = parser.parse(it.date) ?: Date()
                        calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
                    }

                    val filtered = if (mondayIndex != -1) {
                        sortedDays.subList(mondayIndex, sortedDays.size)
                    } else {
                        sortedDays
                    }

                    Log.d("WeatherWeekVM", "Fechas finales: ${filtered.map { it.date }}")
                    _forecastData.value = Forecast(filtered)
                } ?: Log.e("WeatherWeekVM", "forecastDays está vacío o nulo")
            } catch (e: Exception) {
                Log.e("WeatherWeekVM", "Error fatal: ${e.message}")
                _loading.value = false
            }
        }
    }
}
