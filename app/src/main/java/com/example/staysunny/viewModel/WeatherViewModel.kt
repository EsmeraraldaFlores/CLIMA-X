package com.example.staysunny.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.staysunny.model.Weather
import com.example.staysunny.network.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepo: WeatherRepository
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loaderState: LiveData<Boolean> get() = _loading

    private val _weatherData = MutableLiveData<Weather>()
    val weatherInfo: LiveData<Weather> get() = _weatherData

    fun getWeatherDetail(location: String) {
        _loading.value = true

        viewModelScope.launch {
            val result = weatherRepo.getWeatherDetail(location)
            _loading.value = false

            result?.let {
                _weatherData.value = it
            } ?: Log.e("WeatherViewModel", "Fallo en la consulta del clima")
        }
    }
}
