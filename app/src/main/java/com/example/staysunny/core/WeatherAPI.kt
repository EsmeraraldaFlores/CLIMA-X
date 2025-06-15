package com.example.staysunny.core

import com.example.staysunny.model.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("v1/current.json")
    //Este es el endpoint que pide el clima actual en WeatherAPI.
    suspend fun getWeatherInfo(
        @Query("key") apiKey: String,  //Tu clave de API que te da weatherapi.com.
        @Query("q") coordinates: String   // La ciudad o coordenada que quieres buscar.
    ): Response<Weather>   //Respuesta del JSON.


    @GET("v1/forecast.json") // se cambio v1/current.json

    suspend fun getWeatherForecast(
        @Query("key") apiKey: String,  // La clave de API.
        @Query("q") coordinates: String,  // Ciudad o coordenadas.
        @Query("days") days: Int = 7  //Solicitar el pronóstico de 7 días.

    ): Response<Weather> //Respuesta del JSON.
}

