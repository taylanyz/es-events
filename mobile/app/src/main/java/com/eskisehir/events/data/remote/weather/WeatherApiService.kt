package com.eskisehir.events.data.remote.weather

import com.eskisehir.events.data.remote.weather.dto.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude")            latitude: Double,
        @Query("longitude")           longitude: Double,
        @Query("current")             current: String = "temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m",
        @Query("hourly")              hourly: String  = "temperature_2m,apparent_temperature,precipitation_probability,precipitation,weather_code,wind_speed_10m,relative_humidity_2m",
        @Query("timezone")            timezone: String = "Europe/Istanbul",
        @Query("forecast_days")       forecastDays: Int = 7
    ): WeatherResponseDto
}
