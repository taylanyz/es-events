package com.eskisehir.events.data.repository

import com.eskisehir.events.data.remote.weather.WeatherApiService
import com.eskisehir.events.domain.model.CurrentWeather
import com.eskisehir.events.domain.model.HourlyWeather
import com.eskisehir.events.domain.model.WeatherInfo
import com.eskisehir.events.domain.model.WeatherUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherApiService: WeatherApiService
) {
    companion object {
        const val ESKISEHIR_LAT = 39.7767
        const val ESKISEHIR_LNG = 30.5206
    }

    suspend fun getWeather(): Result<WeatherInfo> {
        return try {
            val response = weatherApiService.getWeather(
                latitude = ESKISEHIR_LAT,
                longitude = ESKISEHIR_LNG
            )

            val currentDto = response.current
                ?: return Result.failure(Exception("Mevcut hava durumu verisi alınamadı"))

            val current = CurrentWeather(
                temperature        = currentDto.temperature ?: 0.0,
                apparentTemperature = currentDto.apparentTemperature ?: 0.0,
                humidity           = currentDto.humidity ?: 0,
                weatherCode        = currentDto.weatherCode ?: 0,
                windSpeed          = currentDto.windSpeed ?: 0.0,
                description        = WeatherUtils.getDescription(currentDto.weatherCode ?: 0),
                icon               = WeatherUtils.getIcon(currentDto.weatherCode ?: 0)
            )

            val hourlyDto = response.hourly
            val hourlyList = if (
                hourlyDto != null &&
                !hourlyDto.time.isNullOrEmpty()
            ) {
                val times  = hourlyDto.time!!
                val temps  = hourlyDto.temperature ?: List(times.size) { 0.0 }
                val feels  = hourlyDto.apparentTemperature ?: List(times.size) { 0.0 }
                val precip = hourlyDto.precipitationProbability ?: List(times.size) { 0 }
                val codes  = hourlyDto.weatherCode ?: List(times.size) { 0 }
                val winds  = hourlyDto.windSpeed ?: List(times.size) { 0.0 }
                val hums   = hourlyDto.humidity ?: List(times.size) { 0 }

                times.mapIndexed { i, time ->
                    val code = codes.getOrElse(i) { 0 }
                    HourlyWeather(
                        time                    = time,
                        temperature             = temps.getOrElse(i) { 0.0 },
                        apparentTemperature     = feels.getOrElse(i) { 0.0 },
                        precipitationProbability = precip.getOrElse(i) { 0 },
                        weatherCode             = code,
                        windSpeed               = winds.getOrElse(i) { 0.0 },
                        humidity                = hums.getOrElse(i) { 0 },
                        description             = WeatherUtils.getDescription(code),
                        icon                    = WeatherUtils.getIcon(code)
                    )
                }
            } else emptyList()

            Result.success(WeatherInfo(current = current, hourly = hourlyList))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Finds the closest hourly forecast entry for the given ISO-like datetime string.
     * Returns null if the event date is outside the forecast window or cannot be parsed.
     */
    fun findHourlyForEvent(hourly: List<HourlyWeather>, eventDateStr: String): HourlyWeather? {
        return try {
            // eventDateStr formats we try to handle:
            // "15 Mayıs, 21:00"  →  we derive just the hour
            // "2025-05-15T21:00" →  ISO
            val eventHour = parseEventHour(eventDateStr) ?: return null

            hourly.minByOrNull { slot ->
                val slotHour = parseIsoHour(slot.time) ?: Long.MAX_VALUE
                Math.abs(slotHour - eventHour)
            }.takeIf { closest ->
                val slotHour = parseIsoHour(closest?.time ?: "") ?: return null
                Math.abs(slotHour - eventHour) <= 3 * 3600_000L  // within 3 hours
            }
        } catch (e: Exception) {
            null
        }
    }

    // Returns epoch-millisecond representation of the event's hour.
    private fun parseEventHour(dateStr: String): Long? {
        return try {
            // Try ISO first: "2025-05-15T21:00"
            val isoResult = tryParseIso(dateStr)
            if (isoResult != null) return isoResult

            // Try Turkish format: "15 Mayıs, 21:00"
            val turkishResult = tryParseTurkish(dateStr)
            turkishResult
        } catch (e: Exception) { null }
    }

    private fun tryParseIso(dateStr: String): Long? {
        return try {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm", java.util.Locale.getDefault())
            sdf.timeZone = java.util.TimeZone.getTimeZone("Europe/Istanbul")
            sdf.parse(dateStr.trim())?.time
        } catch (e: Exception) { null }
    }

    private fun tryParseTurkish(dateStr: String): Long? {
        return try {
            // "15 Mayıs, 21:00" → extract day, month, time
            val monthMap = mapOf(
                "Ocak" to 0, "Şubat" to 1, "Mart" to 2, "Nisan" to 3,
                "Mayıs" to 4, "Haziran" to 5, "Temmuz" to 6, "Ağustos" to 7,
                "Eylül" to 8, "Ekim" to 9, "Kasım" to 10, "Aralık" to 11
            )
            val regex = Regex("(\\d{1,2})\\s+(\\w+),?\\s+(\\d{1,2}):(\\d{2})")
            val match = regex.find(dateStr) ?: return null
            val day   = match.groupValues[1].toInt()
            val month = monthMap[match.groupValues[2]] ?: return null
            val hour  = match.groupValues[3].toInt()
            val min   = match.groupValues[4].toInt()

            val cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("Europe/Istanbul"))
            cal.set(java.util.Calendar.MONTH, month)
            cal.set(java.util.Calendar.DAY_OF_MONTH, day)
            cal.set(java.util.Calendar.HOUR_OF_DAY, hour)
            cal.set(java.util.Calendar.MINUTE, min)
            cal.set(java.util.Calendar.SECOND, 0)
            cal.timeInMillis
        } catch (e: Exception) { null }
    }

    private fun parseIsoHour(isoTime: String): Long? {
        return try {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm", java.util.Locale.getDefault())
            sdf.timeZone = java.util.TimeZone.getTimeZone("Europe/Istanbul")
            sdf.parse(isoTime.trim())?.time
        } catch (e: Exception) { null }
    }
}
