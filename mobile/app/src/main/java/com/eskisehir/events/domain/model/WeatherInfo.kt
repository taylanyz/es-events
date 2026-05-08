package com.eskisehir.events.domain.model

/**
 * Domain model for current weather.
 */
data class CurrentWeather(
    val temperature: Double,
    val apparentTemperature: Double,
    val humidity: Int,
    val weatherCode: Int,
    val windSpeed: Double,
    val description: String,
    val icon: String
)

/**
 * Domain model for a single hourly forecast slot.
 */
data class HourlyWeather(
    val time: String,
    val temperature: Double,
    val apparentTemperature: Double,
    val precipitationProbability: Int,
    val weatherCode: Int,
    val windSpeed: Double,
    val humidity: Int,
    val description: String,
    val icon: String
)

/**
 * Full weather response used by the ViewModel.
 */
data class WeatherInfo(
    val current: CurrentWeather,
    val hourly: List<HourlyWeather>
)

/**
 * Maps Open-Meteo weather_code to Turkce description and emoji icon.
 */
object WeatherUtils {

    fun getDescription(code: Int): String = when (code) {
        0            -> "Açık"
        1            -> "Genellikle açık"
        2            -> "Parçalı bulutlu"
        3            -> "Kapalı"
        45, 48       -> "Sisli"
        51, 53, 55   -> "Çiseleme"
        56, 57       -> "Dondurucu çiseleme"
        61, 63, 65   -> "Yağmurlu"
        66, 67       -> "Dondurucu yağmur"
        71, 73, 75   -> "Karlı"
        77           -> "Kar taneleri"
        80, 81, 82   -> "Sağanak yağışlı"
        85, 86       -> "Kar sağanağı"
        95           -> "Gök gürültülü fırtına"
        96, 99       -> "Dolulu fırtına"
        else         -> "Bilinmiyor"
    }

    fun getIcon(code: Int): String = when (code) {
        0            -> "☀️"
        1            -> "🌤"
        2            -> "⛅"
        3            -> "☁️"
        45, 48       -> "🌫"
        51, 53, 55   -> "🌦"
        56, 57       -> "🌧"
        61, 63, 65   -> "🌧"
        66, 67       -> "🌨"
        71, 73, 75   -> "❄️"
        77           -> "🌨"
        80, 81, 82   -> "⛈"
        85, 86       -> "❄️"
        95           -> "⛈"
        96, 99       -> "⛈"
        else         -> "🌡"
    }
}
