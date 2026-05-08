package com.eskisehir.eventapp.ui.screens.explore

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.eskisehir.eventapp.data.model.Category
import com.eskisehir.eventapp.data.model.Event
import com.eskisehir.eventapp.data.model.SampleData
import com.eskisehir.eventapp.ui.components.EventCard
import com.eskisehir.eventapp.ui.components.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(onEventClick: (Long) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    
    // We use sample data as recommendations if API fails
    val recommendedEvents = SampleData.events.shuffled().take(3)

    val filteredEvents = SampleData.events.filter { event ->
        val matchesSearch = searchQuery.isBlank() ||
                event.name.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == null ||
                event.category == selectedCategory
        matchesSearch && matchesCategory
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Keşfet", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Recommendation Section
            item {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    SectionHeader(
                        title = "Sizin için Önerilen", 
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(recommendedEvents) { event ->
                            RecommendationCard(event) { onEventClick(event.id) }
                        }
                    }
                }
            }

            // Search and Filters
            item {
                Column(modifier = Modifier.padding(24.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Etkinlik ara...") },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.primary) },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedCategory == null,
                                onClick = { selectedCategory = null },
                                label = { Text("Tümü") },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                        items(Category.entries.toList()) { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = {
                                    selectedCategory = if (selectedCategory == category) null else category
                                },
                                label = { Text(category.displayNameTr) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            // List Header
            item {
                SectionHeader(
                    title = if (selectedCategory == null) "Tüm Etkinlikler" else selectedCategory!!.displayNameTr,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            // Event List
            items(filteredEvents) { event ->
                Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                    EventCard(
                        event = event,
                        onClick = { onEventClick(event.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendationCard(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(260.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Text Protection Gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = 300f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = event.name,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${event.price.toInt()} ₺",
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
