package com.eskisehir.eventapp.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eskisehir.eventapp.data.model.SampleData
import com.eskisehir.eventapp.ui.components.CompactWeatherCard
import com.eskisehir.eventapp.ui.components.EventCard
import com.eskisehir.eventapp.ui.components.SectionHeader
import com.eskisehir.eventapp.ui.weather.WeatherUiState
import com.eskisehir.eventapp.ui.weather.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onEventClick: (Long) -> Unit,
    weatherViewModel: WeatherViewModel = hiltViewModel()
) {
    val events = SampleData.events
    val weatherUiState by weatherViewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        "Eskişehir\nEvents",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Search, contentDescription = "Ara")
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                CompactWeatherCard(
                    uiState = weatherUiState,
                    onRetry = { weatherViewModel.loadWeather() }
                )
            }

            item {
                SectionHeader(title = "Yaklaşan Etkinlikler")
            }

            items(events) { event ->
                val hourlyWeather = if (weatherUiState is WeatherUiState.Success) {
                    weatherViewModel.getHourlyForEvent(event.date)
                } else null

                EventCard(
                    event = event,
                    onClick = { onEventClick(event.id) },
                    hourlyWeather = hourlyWeather
                )
            }
        }
    }
}
