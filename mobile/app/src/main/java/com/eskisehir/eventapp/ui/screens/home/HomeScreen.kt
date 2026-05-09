package com.eskisehir.eventapp.ui.screens.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.eskisehir.eventapp.data.model.Category
import com.eskisehir.eventapp.data.model.Event
import com.eskisehir.eventapp.data.model.SampleData
import com.eskisehir.eventapp.ui.components.CompactWeatherCard
import com.eskisehir.eventapp.ui.components.EventCard
import com.eskisehir.eventapp.ui.components.SectionHeader
import com.eskisehir.eventapp.ui.weather.WeatherUiState
import com.eskisehir.eventapp.ui.weather.WeatherViewModel
import com.eskisehir.eventapp.util.DateTimeUtils
import com.eskisehir.events.presentation.viewmodel.RoadmapViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onEventClick: (Long) -> Unit,
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    roadmapViewModel: RoadmapViewModel = hiltViewModel()
) {
    val allEvents = SampleData.events
    val today = LocalDate.now().toString()
    val todayEvents = allEvents.filter { it.date.startsWith(today) }
    
    val roadmapUiState by roadmapViewModel.uiState.collectAsState()
    
    LaunchedEffect(today) {
        Log.d("TODAY_EVENTS", "Today=$today count=${todayEvents.size}")
    }

    val weatherUiState by weatherViewModel.uiState.collectAsState()
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    
    val displayEvents = if (selectedCategory == null) {
        allEvents
    } else {
        allEvents.filter { it.category == selectedCategory }
    }

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
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Weather Summary
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    CompactWeatherCard(
                        uiState = weatherUiState,
                        onRetry = { weatherViewModel.loadWeather() }
                    )
                }
            }

            // Today's Events Section
            if (selectedCategory == null) {
                if (todayEvents.isNotEmpty()) {
                    item {
                        Column {
                            SectionHeader(
                                title = "Bugünün Etkinlikleri",
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                            )
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 24.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                items(todayEvents) { event ->
                                    val isInRoadmap = roadmapUiState.stops.any { it.eventId == event.id }
                                    SmallEventCardWithAction(
                                        event = event,
                                        isInRoadmap = isInRoadmap,
                                        onClick = { onEventClick(event.id) },
                                        onActionClick = {
                                            if (isInRoadmap) {
                                                roadmapViewModel.removeStop(event.id)
                                            } else {
                                                roadmapViewModel.addStop(
                                                    event.id, event.name, event.latitude, event.longitude, event.venue, event.address, event.date
                                                )
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                } else {
                    item {
                        Text(
                            "Bugün için etkinlik bulunamadı.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                }
            }

            // Categories Filter
            item {
                Column {
                    SectionHeader(
                        title = "Kategoriler",
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedCategory == null,
                                onClick = { selectedCategory = null },
                                label = { Text("Tümü") },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                        items(Category.entries) { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { selectedCategory = if (selectedCategory == category) null else category },
                                label = { Text(category.displayNameTr) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // Main List Section
            item {
                SectionHeader(
                    title = if (selectedCategory == null) "Tüm Etkinlikler" else selectedCategory!!.displayNameTr,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }

            items(displayEvents) { event ->
                val hourlyWeather = if (weatherUiState is WeatherUiState.Success) {
                    weatherViewModel.getHourlyForEvent(event.date)
                } else null

                Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                    EventCard(
                        event = event,
                        onClick = { onEventClick(event.id) },
                        hourlyWeather = hourlyWeather
                    )
                }
            }
        }
    }
}

@Composable
fun SmallEventCardWithAction(
    event: Event,
    isInRoadmap: Boolean,
    onClick: () -> Unit,
    onActionClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column {
            Box {
                AsyncImage(
                    model = event.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = onActionClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = if (isInRoadmap) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        contentColor = if (isInRoadmap) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = if (isInRoadmap) Icons.Default.Route else Icons.Default.AddLocationAlt,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = event.name,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = DateTimeUtils.formatEventTime(event.date),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = event.venue,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
