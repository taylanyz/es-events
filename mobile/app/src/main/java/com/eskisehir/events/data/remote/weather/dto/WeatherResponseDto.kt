package com.eskisehir.events.data.remote.weather.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponseDto(
    @SerializedName("current") val current: CurrentWeatherDto?,
    @SerializedName("hourly")  val hourly: HourlyWeatherDto?
)

data class CurrentWeatherDto(
    @SerializedName("time")                   val time: String?,
    @SerializedName("temperature_2m")         val temperature: Double?,
    @SerializedName("apparent_temperature")   val apparentTemperature: Double?,
    @SerializedName("relative_humidity_2m")   val humidity: Int?,
    @SerializedName("weather_code")           val weatherCode: Int?,
    @SerializedName("wind_speed_10m")         val windSpeed: Double?
)

data class HourlyWeatherDto(
    @SerializedName("time")                      val time: List<String>?,
    @SerializedName("temperature_2m")            val temperature: List<Double>?,
    @SerializedName("apparent_temperature")      val apparentTemperature: List<Double>?,
    @SerializedName("precipitation_probability") val precipitationProbability: List<Int>?,
    @SerializedName("precipitation")             val precipitation: List<Double>?,
    @SerializedName("weather_code")              val weatherCode: List<Int>?,
    @SerializedName("wind_speed_10m")            val windSpeed: List<Double>?,
    @SerializedName("relative_humidity_2m")      val humidity: List<Int>?
)
