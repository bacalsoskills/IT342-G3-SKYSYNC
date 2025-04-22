package com.frontend.mobile.model

data class DailyWeatherDTO(
    val date: String,
    val minTemp: Double,
    val maxTemp: Double,
    val weatherCode: Int,
    val weatherDescription: String,
    val dayOfWeek: String
)