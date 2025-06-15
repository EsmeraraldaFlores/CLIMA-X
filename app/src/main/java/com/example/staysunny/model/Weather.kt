package com.example.staysunny.model

import com.google.gson.annotations.SerializedName

data class Weather(
    val location: Location,
    val current: Current,
    val forecast: Forecast // <-- Agregar el pronóstico futuro
) {
    data class Forecast(
        @SerializedName("forecastday") val forecastDays: List<ForecastDay>
    )

    data class ForecastDay(
        val date: String,
        @SerializedName("day") val dayInfo: DayInfo
    )

    data class DayInfo(
        @SerializedName("avgtemp_c") val avgTempC: Float,
        @SerializedName("maxwind_kph") val windSpeed: Float, // Agregar viento
        @SerializedName("daily_chance_of_rain") val rainChance: Int, // Agregar probabilidad de lluvia
        val condition: Condition
    )


    data class Location(
        val name: String,
        val region: String,
        val country: String,
        val lat: Float,
        val lon: Float,
        val localtime: String
    )

    data class Current(
        @SerializedName("last_updated") val lastUpdated: String,
        @SerializedName("temp_c") val tempC: Float,
        @SerializedName("is_day") val isDay: Int,
        val condition: Condition,
        @SerializedName("wind_mph")val windMph: Float
    )

    data class Condition(
        val text: String,
        val icon: String,
        val code: Int
    )

}

