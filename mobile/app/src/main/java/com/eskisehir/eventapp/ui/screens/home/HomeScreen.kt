package com.eskisehir.eventapp.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onEventClick: (Long) -> Unit,
    weatherViewModel: WeatherViewModel = hiltViewModel()
) {
    val allEvents = SampleData.events
    val featuredEvents = allEvents.filter { it.isFeatured }
    val workshopEvents = allEvents.filter { it.category == Category.WORKSHOP }.take(6)
    val concertEvents = allEvents.filter { it.category == Category.CONCERT }.take(6)
    val cultureEvents = allEvents.filter { it.category == Category.CULTURE || it.category == Category.MUSEUM }.take(6)
    val outdoorEvents = allEvents.filter { it.category == Category.PARK || it.category == Category.WALKING_ROUTE }.take(6)
    
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

            // Featured Section
            if (selectedCategory == null && featuredEvents.isNotEmpty()) {
                item {
                    Column {
                        SectionHeader(
                            title = "Öne Çıkanlar",
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            items(featuredEvents) { event ->
                                FeaturedEventCard(event) { onEventClick(event.id) }
                            }
                        }
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

            if (selectedCategory == null) {
                // Section: Workshops
                if (workshopEvents.isNotEmpty()) {
                    item {
                        HomeHorizontalSection(
                            title = "Workshop & Atölye",
                            events = workshopEvents,
                            onEventClick = onEventClick
                        )
                    }
                }

                // Section: Concerts
                if (concertEvents.isNotEmpty()) {
                    item {
                        HomeHorizontalSection(
                            title = "Konser & Canlı Müzik",
                            events = concertEvents,
                            onEventClick = onEventClick
                        )
                    }
                }

                // Section: Culture & Museum
                if (cultureEvents.isNotEmpty()) {
                    item {
                        HomeHorizontalSection(
                            title = "Kültür & Sanat",
                            events = cultureEvents,
                            onEventClick = onEventClick
                        )
                    }
                }

                // Section: Parks & Walking
                if (outdoorEvents.isNotEmpty()) {
                    item {
                        HomeHorizontalSection(
                            title = "Parklar & Rotalar",
                            events = outdoorEvents,
                            onEventClick = onEventClick
                        )
                    }
                }
            }

            // Main Event List (or filtered results)
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
fun HomeHorizontalSection(
    title: String,
    events: List<Event>,
    onEventClick: (Long) -> Unit
) {
    Column {
        SectionHeader(
            title = title,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            items(events) { event ->
                SmallEventCard(event) { onEventClick(event.id) }
            }
        }
    }
}

@Composable
fun FeaturedEventCard(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(180.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(12.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        event.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        event.venue,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun SmallEventCard(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = event.name,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = DateTimeUtils.formatEventDateShort(event.date),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = event.category.displayNameTr,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
