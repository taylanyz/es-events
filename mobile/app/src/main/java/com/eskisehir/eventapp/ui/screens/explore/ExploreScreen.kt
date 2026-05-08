package com.eskisehir.eventapp.ui.screens.explore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eskisehir.eventapp.data.model.Category
import com.eskisehir.eventapp.data.model.Event
import com.eskisehir.eventapp.data.model.SampleData
import com.eskisehir.eventapp.ui.components.EventCard
import com.eskisehir.events.data.remote.api.EventApiService
import com.eskisehir.events.data.remote.dto.RecommendationRequestDto
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Explore Screen - Search and filter events by category.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(onEventClick: (Long) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var recommendedEvents by remember { mutableStateOf<List<Event>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
                val client = OkHttpClient.Builder().addInterceptor(logging).build()
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8081/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val api = retrofit.create(EventApiService::class.java)
                val recommendationRequest = RecommendationRequestDto(
                    preferredCategories = emptyList(),
                    preferredTags = emptyList(),
                    maxPrice = null,
                    limit = 5
                )
                val recs = api.getRecommendations(recommendationRequest)
                recommendedEvents = recs.map { dto ->
                    Event(
                        id = dto.id,
                        name = dto.name,
                        description = dto.description,
                        category = Category.valueOf(dto.category),
                        latitude = dto.latitude,
                        longitude = dto.longitude,
                        venue = dto.venue,
                        date = dto.date,
                        price = dto.price,
                        imageUrl = dto.imageUrl ?: "",
                        tags = dto.tags ?: emptyList()
                    )
                }
            } catch (e: Exception) {
                android.util.Log.e("ExploreScreen", "Failed to fetch recommendations: ${e.message}")
                recommendedEvents = SampleData.events.take(3)
            }
        }
    }

    val filteredEvents = SampleData.events.filter { event ->
        val matchesSearch = searchQuery.isBlank() ||
                event.name.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == null ||
                event.category == selectedCategory
        matchesSearch && matchesCategory
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Keşfet", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Recommended events section
            if (recommendedEvents.isNotEmpty()) {
                Text(
                    "Sizin için Önerilen",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.height(200.dp)
                ) {
                    items(recommendedEvents) { event ->
                        Card(
                            modifier = Modifier
                                .width(150.dp)
                                .fillMaxHeight(),
                            onClick = { onEventClick(event.id) }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(event.name, fontWeight = FontWeight.Bold, maxLines = 2)
                                Text("${event.price.toInt()} ₺", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Etkinlik ara...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = MaterialTheme.shapes.large
            )

            // Category filter chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("Tümü") }
                    )
                }
                items(Category.entries.toList()) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = if (selectedCategory == category) null else category
                        },
                        label = { Text(category.displayNameTr) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Filtered event list
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredEvents) { event ->
                    EventCard(
                        event = event,
                        onClick = { onEventClick(event.id) }
                    )
                }
            }
        }
    }
}
