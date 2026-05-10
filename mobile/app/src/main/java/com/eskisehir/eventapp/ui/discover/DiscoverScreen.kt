package com.eskisehir.eventapp.ui.discover

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        if (uiState is RecommendationUiState.Error) {
            snackbarHostState.showSnackbar(
                message = (uiState as RecommendationUiState.Error).message,
                duration = SnackbarDuration.Long
            )
            viewModel.reset()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Yapay Zeka Keşfi", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    if (uiState is RecommendationUiState.Success) {
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
                is RecommendationUiState.Idle, is RecommendationUiState.Error -> {
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
                "Detaylı tercihlerine göre yapay zeka en doğru etkinlikleri eşleştirsin.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Questions sections...
        item {
            QuestionSection(title = "Hangi tür etkinliklerle ilgileniyorsun?", icon = Icons.Default.Category) {
                SimpleFlowRow(modifier = Modifier.fillMaxWidth(), spacing = 8.dp) {
                    Category.entries.filter { it != Category.OTHER }.forEach { category ->
                        val isSelected = preferences.selectedCategories.contains(category)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                val newList = if (isSelected) preferences.selectedCategories - category else preferences.selectedCategories + category
                                onPrefsChanged(preferences.copy(selectedCategories = newList))
                            },
                            label = { Text(category.displayNameTr) },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        item {
            QuestionSection(title = "Kalabalık durumu nasıl olsun?", icon = Icons.Default.Groups) {
                SimpleFlowRow(modifier = Modifier.fillMaxWidth(), spacing = 8.dp) {
                    CrowdPreference.entries.forEach { crowd ->
                        FilterChip(
                            selected = preferences.crowd == crowd,
                            onClick = { onPrefsChanged(preferences.copy(crowd = crowd)) },
                            label = { Text(crowd.label) },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        item {
            QuestionSection(title = "Etkinliğe nasıl gitmek istiyorsun?", icon = Icons.Default.Person) {
                SimpleFlowRow(modifier = Modifier.fillMaxWidth(), spacing = 8.dp) {
                    CompanionPreference.entries.forEach { companion ->
                        FilterChip(
                            selected = preferences.companion == companion,
                            onClick = { onPrefsChanged(preferences.copy(companion = companion)) },
                            label = { Text(companion.label) },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        item {
            QuestionSection(title = "Hava nasıl olsun?", icon = Icons.Default.Cloud) {
                SimpleFlowRow(modifier = Modifier.fillMaxWidth(), spacing = 8.dp) {
                    FilterChip(
                        selected = preferences.isIndoor == false,
                        onClick = { onPrefsChanged(preferences.copy(isIndoor = false)) },
                        label = { Text("Açık Hava") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    FilterChip(
                        selected = preferences.isIndoor == true,
                        onClick = { onPrefsChanged(preferences.copy(isIndoor = true)) },
                        label = { Text("Kapalı Mekan") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    FilterChip(
                        selected = preferences.isIndoor == null,
                        onClick = { onPrefsChanged(preferences.copy(isIndoor = null)) },
                        label = { Text("Fark Etmez") },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        item {
            QuestionSection(title = "Etkinlik zamanı nasıl olsun?", icon = Icons.Default.Schedule) {
                SimpleFlowRow(modifier = Modifier.fillMaxWidth(), spacing = 8.dp) {
                    TimeOfDayPreference.entries.forEach { time ->
                        FilterChip(
                            selected = preferences.timeOfDay == time,
                            onClick = { onPrefsChanged(preferences.copy(timeOfDay = time)) },
                            label = { Text(time.label) },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        item {
            QuestionSection(title = "Bütçe tercihin ne olsun?", icon = Icons.Default.Payments) {
                SimpleFlowRow(modifier = Modifier.fillMaxWidth(), spacing = 8.dp) {
                    BudgetPreference.entries.forEach { budget ->
                        FilterChip(
                            selected = preferences.budget == budget,
                            onClick = { onPrefsChanged(preferences.copy(budget = budget)) },
                            label = { Text(budget.label) },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        item {
            QuestionSection(title = "Ulaşım tercihin nedir?", icon = Icons.Default.DirectionsCar) {
                SimpleFlowRow(modifier = Modifier.fillMaxWidth(), spacing = 8.dp) {
                    TransportPreference.entries.forEach { transport ->
                        FilterChip(
                            selected = preferences.transport == transport,
                            onClick = { onPrefsChanged(preferences.copy(transport = transport)) },
                            label = { Text(transport.label) },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        item {
            QuestionSection(title = "Etkinlik süresi nasıl olsun?", icon = Icons.Default.Timer) {
                SimpleFlowRow(modifier = Modifier.fillMaxWidth(), spacing = 8.dp) {
                    DurationPreference.entries.forEach { duration ->
                        FilterChip(
                            selected = preferences.duration == duration,
                            onClick = { onPrefsChanged(preferences.copy(duration = duration)) },
                            label = { Text(duration.label) },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        item {
            QuestionSection(title = "Sosyal ortam tercihin nasıl?", icon = Icons.Default.AutoAwesome) {
                SimpleFlowRow(modifier = Modifier.fillMaxWidth(), spacing = 8.dp) {
                    SocialMoodPreference.entries.forEach { mood ->
                        FilterChip(
                            selected = preferences.socialMood == mood,
                            onClick = { onPrefsChanged(preferences.copy(socialMood = mood)) },
                            label = { Text(mood.label) },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = onRecommendClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.AutoAwesome, null)
                Spacer(Modifier.width(8.dp))
                Text("Bana Etkinlik Öner", fontWeight = FontWeight.Bold)
            }
        }
        item { Spacer(Modifier.height(32.dp)) }
    }
}

@Composable
fun QuestionSection(title: String, icon: ImageVector, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(12.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(12.dp))
        content()
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
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
        Text("Yapay zeka etkinlikleri eşleştiriyor...", style = MaterialTheme.typography.titleMedium)
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
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Text("Size Özel Önerilerimiz", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(8.dp))
        }

        itemsIndexed(recommendations) { index, (event, rec) ->
            AiRecommendationCard(
                event = event,
                rec = rec,
                isTopRecommendation = index == 0,
                onClick = { onEventClick(event.id) }
            )
        }
    }
}

@Composable
fun AiRecommendationCard(
    event: Event,
    rec: EventRecommendation,
    isTopRecommendation: Boolean = false,
    onClick: () -> Unit
) {
    val cardColor = if (isTopRecommendation) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f) 
                    else MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isTopRecommendation) 6.dp else 2.dp),
        border = if (isTopRecommendation) BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)) else null
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (isTopRecommendation) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.AutoAwesome, null, modifier = Modifier.size(16.dp), tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Yapay zekamız sizin için öneriyor",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Image section - Edge to edge
            Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                AsyncImage(
                    model = event.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = CircleShape
                ) {
                    Text(
                        "%${rec.score} Uyum",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            // Content section - Standardized padding and alignment
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = event.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Reason Box - Full width within padding
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Neden Önerildi?",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = rec.reason,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                
                // Tags section
                SimpleFlowRow(modifier = Modifier.fillMaxWidth(), spacing = 8.dp) {
                    SuggestionChip(
                        onClick = {},
                        label = { Text(event.category.displayNameTr) },
                        shape = CircleShape,
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            labelColor = MaterialTheme.colorScheme.primary
                        ),
                        border = null
                    )
                    
                    rec.matchedPreferences.forEach { pref ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text(pref) },
                            shape = CircleShape,
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
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
        var currentRowHeight = 0
        
        val positions = placeables.map { placeable ->
            if (x + placeable.width > constraints.maxWidth) {
                x = 0
                y += currentRowHeight + spacing.roundToPx()
                currentRowHeight = 0
            }
            val pos = androidx.compose.ui.unit.IntOffset(x, y)
            x += placeable.width + spacing.roundToPx()
            currentRowHeight = maxOf(currentRowHeight, placeable.height)
            pos
        }
        
        layout(constraints.maxWidth, if (placeables.isEmpty()) 0 else y + currentRowHeight) {
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(positions[index])
            }
        }
    }
}
