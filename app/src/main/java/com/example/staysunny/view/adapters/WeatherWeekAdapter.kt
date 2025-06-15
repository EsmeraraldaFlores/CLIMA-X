package com.example.staysunny.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.staysunny.databinding.ItemWeatherWeekBinding
import com.example.staysunny.model.Weather.ForecastDay
import android.util.Log


class WeatherWeekAdapter(private var forecastList: List<ForecastDay>) :
    RecyclerView.Adapter<WeatherWeekAdapter.WeatherWeekViewHolder>() {

    inner class WeatherWeekViewHolder(private val binding: ItemWeatherWeekBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(forecast: ForecastDay) {
            binding.tvDateWeek.text = forecast.date
            binding.tvWeatherConditionWeek.text = forecast.dayInfo.condition.text
            binding.tvCelsiusWeek.text = "${forecast.dayInfo.avgTempC} °C"
            binding.tvWindInfoWeek.text = "Wind: ${forecast.dayInfo.windSpeed} km/h"
            binding.tvRainProbabilityWeek.text = "Rain probability: ${forecast.dayInfo.rainChance}%"

            Glide.with(binding.root.context)
                .load("https:" + forecast.dayInfo.condition.icon)
                .into(binding.ivWeatherIconWeek)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherWeekViewHolder {
        val binding =
            ItemWeatherWeekBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherWeekViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherWeekViewHolder, position: Int) {
        holder.bind(forecastList[position])
    }

    override fun getItemCount(): Int = forecastList.size

    // Método para actualizar la lista de datos
    fun updateData(newList: List<ForecastDay>) {
        val previousSize = forecastList.size
        forecastList = newList
        notifyItemRangeInserted(previousSize, forecastList.size) // ✅ Refresca solo los nuevos elementos
    }

}
