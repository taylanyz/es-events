package com.eskisehir.eventapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eskisehir.eventapp.ui.weather.WeatherUiState
import com.eskisehir.events.domain.model.CurrentWeather
import com.eskisehir.events.domain.model.HourlyWeather

// ─────────────────────────────────────────────────────────
// Main WeatherCard — shown on HomeScreen
// ─────────────────────────────────────────────────────────
@Composable
fun WeatherCard(
    uiState: WeatherUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is WeatherUiState.Loading ->
            WeatherLoadingState(modifier)
        is WeatherUiState.Error ->
            WeatherErrorState(message = uiState.message, onRetry = onRetry, modifier = modifier)
        is WeatherUiState.Success ->
            WeatherContent(weather = uiState.data.current, modifier = modifier)
    }
}

@Composable
private fun WeatherContent(weather: CurrentWeather, modifier: Modifier = Modifier) {
    val gradientColors = listOf(Color(0xFF1565C0), Color(0xFF42A5F5))

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(gradientColors))
                .padding(20.dp)
        ) {
            Column {
                // City + icon row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "Eskişehir",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White.copy(alpha = 0.85f),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "${weather.temperature.toInt()}°C",
                            style = MaterialTheme.typography.displaySmall,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = weather.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    Text(text = weather.icon, fontSize = 56.sp)
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.25f))
                Spacer(Modifier.height(14.dp))

                // Detail row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherDetailItem(
                        icon = "🌡",
                        label = "Hissedilen",
                        value = "${weather.apparentTemperature.toInt()}°C"
                    )
                    WeatherDetailItem(
                        icon = "💧",
                        label = "Nem",
                        value = "%${weather.humidity}"
                    )
                    WeatherDetailItem(
                        icon = "💨",
                        label = "Rüzgar",
                        value = "${weather.windSpeed.toInt()} km/h"
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherDetailItem(icon: String, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = icon, fontSize = 20.sp)
        Spacer(Modifier.height(4.dp))
        Text(text = value, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
        Text(text = label, color = Color.White.copy(alpha = 0.75f), style = MaterialTheme.typography.labelSmall)
    }
}

// ─────────────────────────────────────────────────────────
// EventWeatherInfo — shown on EventDetailScreen
// ─────────────────────────────────────────────────────────
@Composable
fun EventWeatherInfo(
    uiState: WeatherUiState,
    eventDateStr: String,
    findHourly: (String) -> HourlyWeather?,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is WeatherUiState.Loading ->
            WeatherLoadingState(modifier = modifier, compact = true)
        is WeatherUiState.Error ->
            EventWeatherUnavailable(modifier)
        is WeatherUiState.Success -> {
            val hourly = findHourly(eventDateStr)
            if (hourly == null) {
                EventWeatherOutOfRange(modifier)
            } else {
                EventWeatherDetail(hourly = hourly, modifier = modifier)
            }
        }
    }
}

@Composable
private fun EventWeatherDetail(hourly: HourlyWeather, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = hourly.icon, fontSize = 28.sp)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = "${hourly.temperature.toInt()}°C  •  ${hourly.description}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Hissedilen: ${hourly.apparentTemperature.toInt()}°C",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EventWeatherChip(label = "🌧 Yağış", value = "%${hourly.precipitationProbability}")
                EventWeatherChip(label = "💨 Rüzgar", value = "${hourly.windSpeed.toInt()} km/h")
                EventWeatherChip(label = "💧 Nem", value = "%${hourly.humidity}")
            }
        }
    }
}

@Composable
private fun EventWeatherChip(label: String, value: String) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.ExtraBold)
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        }
    }
}

// ─────────────────────────────────────────────────────────
// Shimmer Loading State
// ─────────────────────────────────────────────────────────
@Composable
fun WeatherLoadingState(modifier: Modifier = Modifier, compact: Boolean = false) {
    val transition = rememberInfiniteTransition(label = "weatherShimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f, targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "shimmerAnim"
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(if (compact) 80.dp else 160.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFE0E0E0), Color(0xFFF5F5F5), Color(0xFFE0E0E0)),
                    start = Offset(translateAnim - 500f, 0f),
                    end = Offset(translateAnim, 0f)
                )
            )
    )
}

// ─────────────────────────────────────────────────────────
// Error States
// ─────────────────────────────────────────────────────────
@Composable
fun WeatherErrorState(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onRetry) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Tekrar dene",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

@Composable
fun EventWeatherUnavailable(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = "⚠️  Hava durumu bilgisi alınamadı.",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun EventWeatherOutOfRange(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "🗓", fontSize = 22.sp)
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Hava tahmini etkinlik tarihine yaklaştıkça gösterilecek.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


// ─────────────────────────────────────────────────────────
// Compact WeatherCard — küçük özet, HomeScreen için
// ─────────────────────────────────────────────────────────
@Composable
fun CompactWeatherCard(
    uiState: WeatherUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is WeatherUiState.Loading ->
            WeatherLoadingState(modifier = modifier, compact = true)
        is WeatherUiState.Error ->
            WeatherErrorState(message = uiState.message, onRetry = onRetry, modifier = modifier)
        is WeatherUiState.Success -> {
            val w = uiState.data.current
            Surface(
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFF1565C0).copy(alpha = 0.12f),
                tonalElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = w.icon, fontSize = 28.sp)
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "${w.temperature.toInt()}°C  •  ${w.description}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Eskişehir",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("💧", fontSize = 14.sp)
                            Text("%${w.humidity}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("💨", fontSize = 14.sp)
                            Text("${w.windSpeed.toInt()}km/h", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
