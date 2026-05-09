package com.eskisehir.eventapp.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.eskisehir.eventapp.data.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    onBackClick: () -> Unit,
    onEventClick: (Long) -> Unit,
    viewModel: DiscoverViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val preferences by viewModel.preferences.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Yapay Zeka Keşfi", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    if (uiState !is RecommendationUiState.Idle) {
                        IconButton(onClick = { viewModel.reset() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Sıfırla")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (uiState) {
                is RecommendationUiState.Idle -> {
                    PreferenceForm(
                        preferences = preferences,
                        onPrefsChanged = { viewModel.updatePreferences(it) },
                        onRecommendClick = { viewModel.getRecommendations() }
                    )
                }
                is RecommendationUiState.Loading -> {
                    LoadingState()
                }
                is RecommendationUiState.Success -> {
                    val results = (uiState as RecommendationUiState.Success).recommendations
                    RecommendationsList(results, onEventClick)
                }
                is RecommendationUiState.Error -> {
                    ErrorState((uiState as RecommendationUiState.Error).message) { viewModel.reset() }
                }
            }
        }
    }
}

@Composable
fun PreferenceForm(
    preferences: RecommendationPreferences,
    onPrefsChanged: (RecommendationPreferences) -> Unit,
    onRecommendClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Text(
                "Sana uygun etkinliği bulalım",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black
            )
            Text(
                "Birkaç soruya cevap ver, yapay zeka sana en uygun etkinlikleri önersin.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            QuestionCard(title = "Ne zaman etkinliğe gitmek istiyorsun?", icon = Icons.Default.Event) {
                SimpleFlowRow(modifier = Modifier.fillMaxWidth(), spacing = 8.dp) {
                    TimePreference.entries.forEach { time ->
                        FilterChip(
                            selected = preferences.time == time,
                            onClick = { onPrefsChanged(preferences.copy(time = time)) },
                            label = { Text(time.label) }
                        )
                    }
                }
            }
        }

        item {
            QuestionCard(title = "Bütçen nedir?", icon = Icons.Default.Payments) {
                SimpleFlowRow(modifier = Modifier.fillMaxWidth(), spacing = 8.dp) {
                    BudgetPreference.entries.forEach { budget ->
                        FilterChip(
                            selected = preferences.budget == budget,
                            onClick = { onPrefsChanged(preferences.copy(budget = budget)) },
                            label = { Text(budget.label) }
                        )
                    }
                }
            }
        }

        item {
            QuestionCard(title = "Nasıl bir ortam istersin?", icon = Icons.Default.Groups) {
                SimpleFlowRow(modifier = Modifier.fillMaxWidth(), spacing = 8.dp) {
                    CrowdPreference.entries.forEach { crowd ->
                        FilterChip(
                            selected = preferences.crowd == crowd,
                            onClick = { onPrefsChanged(preferences.copy(crowd = crowd)) },
                            label = { Text(crowd.label) }
                        )
                    }
                }
            }
        }

        item {
            QuestionCard(title = "Hangi türler ilgini çekiyor?", icon = Icons.Default.Category) {
                SimpleFlowRow(modifier = Modifier.fillMaxWidth(), spacing = 8.dp) {
                    Category.entries.filter { it != Category.OTHER }.forEach { category ->
                        val isSelected = preferences.selectedCategories.contains(category)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                val newList = if (isSelected) {
                                    preferences.selectedCategories - category
                                } else {
                                    preferences.selectedCategories + category
                                }
                                onPrefsChanged(preferences.copy(selectedCategories = newList))
                            },
                            label = { Text(category.displayNameTr) }
                        )
                    }
                }
            }
        }

        item {
            QuestionCard(title = "Ulaşım tercihin nedir?", icon = Icons.Default.Directions) {
                SimpleFlowRow(modifier = Modifier.fillMaxWidth(), spacing = 8.dp) {
                    TransportPreference.entries.forEach { transport ->
                        FilterChip(
                            selected = preferences.transport == transport,
                            onClick = { onPrefsChanged(preferences.copy(transport = transport)) },
                            label = { Text(transport.label) }
                        )
                    }
                }
            }
        }

        item {
            QuestionCard(title = "Mekan tercihi", icon = Icons.Default.HomeWork) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = preferences.isIndoor == true,
                        onClick = { onPrefsChanged(preferences.copy(isIndoor = true)) },
                        label = { Text("Kapalı Mekan") }
                    )
                    FilterChip(
                        selected = preferences.isIndoor == false,
                        onClick = { onPrefsChanged(preferences.copy(isIndoor = false)) },
                        label = { Text("Açık Hava") }
                    )
                    FilterChip(
                        selected = preferences.isIndoor == null,
                        onClick = { onPrefsChanged(preferences.copy(isIndoor = null)) },
                        label = { Text("Fark Etmez") }
                    )
                }
            }
        }

        item {
            QuestionCard(title = "Kiminle gideceksin?", icon = Icons.Default.Person) {
                SimpleFlowRow(modifier = Modifier.fillMaxWidth(), spacing = 8.dp) {
                    CompanionPreference.entries.forEach { comp ->
                        FilterChip(
                            selected = preferences.companion == comp,
                            onClick = { onPrefsChanged(preferences.copy(companion = comp)) },
                            label = { Text(comp.label) }
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = onRecommendClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.AutoAwesome, null)
                Spacer(Modifier.width(8.dp))
                Text("Bana Etkinlik Öner", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun QuestionCard(title: String, icon: ImageVector, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(12.dp))
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun LoadingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(64.dp))
        Spacer(Modifier.height(24.dp))
        Text(
            "Yapay zeka etkinlikleri inceliyor...",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        Text(
            "Tercihlerine en uygun olanları seçiyoruz.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.ErrorOutline, null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(16.dp))
        Text(message, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onRetry) {
            Text("Tekrar Dene")
        }
    }
}

@Composable
fun RecommendationsList(
    recommendations: List<Pair<Event, EventRecommendation>>,
    onEventClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                "İşte Senin İçin Seçtiklerimiz",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black
            )
            Spacer(Modifier.height(8.dp))
        }

        items(recommendations) { (event, rec) ->
            AiRecommendationCard(event, rec) { onEventClick(event.id) }
        }
    }
}

@Composable
fun AiRecommendationCard(event: Event, rec: EventRecommendation, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                AsyncImage(
                    model = event.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = CircleShape
                ) {
                    Text(
                        "%${rec.score}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Text(event.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                
                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Neden Önerildi?",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            rec.reason,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                
                SimpleFlowRow(modifier = Modifier.fillMaxWidth(), spacing = 6.dp) {
                    rec.matchedPreferences.forEach { pref ->
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                            shape = CircleShape
                        ) {
                            Text(
                                pref,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleFlowRow(
    modifier: Modifier = Modifier,
    spacing: androidx.compose.ui.unit.Dp = 0.dp,
    content: @Composable () -> Unit
) {
    androidx.compose.ui.layout.Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        var x = 0
        var y = 0
        val positions = placeables.map { placeable ->
            if (x + placeable.width > constraints.maxWidth) {
                x = 0
                y += (placeables.firstOrNull()?.height ?: 0) + spacing.roundToPx()
            }
            val pos = IntOffset(x, y)
            x += placeable.width + spacing.roundToPx()
            pos
        }
        layout(constraints.maxWidth, y + (placeables.lastOrNull()?.height ?: 0)) {
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(positions[index])
            }
        }
    }
}
