package com.eskisehir.eventapp.ui.screens.detail

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.eskisehir.eventapp.ui.components.CategoryFallbackBox
import com.eskisehir.eventapp.ui.components.EventImageUtils
import com.eskisehir.eventapp.ui.components.EventWeatherInfo
import com.eskisehir.eventapp.ui.components.ModernStatusChip
import com.eskisehir.eventapp.ui.components.SectionHeader
import com.eskisehir.eventapp.ui.components.ShimmerPlaceholder
import com.eskisehir.eventapp.ui.viewmodels.EventDetailViewModel
import com.eskisehir.eventapp.ui.weather.WeatherViewModel
import com.eskisehir.eventapp.util.DateTimeUtils
import com.eskisehir.events.data.local.entity.EventStatus
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: Long,
    onBackClick: () -> Unit,
    onMapClick: ((Long) -> Unit)? = null,
    viewModel: EventDetailViewModel = hiltViewModel(),
    weatherViewModel: WeatherViewModel = hiltViewModel()
) {
    val event by viewModel.event.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val status by viewModel.status.collectAsState()
    val comments by viewModel.getComments(eventId).collectAsState(initial = emptyList())
    val weatherUiState by weatherViewModel.uiState.collectAsState()

    var commentText by remember { mutableStateOf("") }

    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    Surface(
                        modifier = Modifier.padding(8.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        shadowElevation = 4.dp
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                        }
                    }
                },
                actions = {
                    Surface(
                        modifier = Modifier.padding(4.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        shadowElevation = 4.dp
                    ) {
                        IconButton(onClick = { onMapClick?.invoke(eventId) }) {
                            Icon(Icons.Default.Map, contentDescription = "Harita")
                        }
                    }
                    Surface(
                        modifier = Modifier.padding(8.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        shadowElevation = 4.dp
                    ) {
                        IconButton(onClick = { viewModel.toggleFavorite(eventId) }) {
                            Icon(
                                if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favori",
                                tint = if (isFavorite) Color.Red else LocalContentColor.current
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        if (event == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val currentEvent = event!!
            val effectiveImageUrl = EventImageUtils.getEffectiveImageUrl(currentEvent.imageUrl, currentEvent.category)
            val categoryColor = EventImageUtils.getCategoryColor(currentEvent.category)
            val formattedDate = DateTimeUtils.formatEventDate(currentEvent.date)

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    // Large Premium Image Header with gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(380.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(effectiveImageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = currentEvent.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp)),
                            contentScale = ContentScale.Crop
                        ) {
                            when (painter.state) {
                                is AsyncImagePainter.State.Loading -> ShimmerPlaceholder()
                                is AsyncImagePainter.State.Error   -> CategoryFallbackBox(categoryColor, currentEvent.category.displayNameTr)
                                else                               -> SubcomposeAsyncImageContent()
                            }
                        }

                        // Bottom gradient
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .align(Alignment.BottomCenter)
                                .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f))
                                    )
                                )
                        )

                        // Price badge
                        Surface(
                            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
                            color = categoryColor,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = if (currentEvent.price == 0.0) "Ücretsiz" else "${currentEvent.price.toInt()} ₺",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                item {
                    Column(modifier = Modifier.padding(24.dp)) {
                        // Category badge
                        Surface(
                            color = categoryColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = currentEvent.category.displayNameTr,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = categoryColor,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = currentEvent.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Info Row Cards
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InfoCard(Icons.Default.CalendarToday, "Tarih & Saat", formattedDate, categoryColor, Modifier.weight(1f))
                            InfoCard(Icons.Default.Place, "Konum", currentEvent.venue, categoryColor, Modifier.weight(1f))
                        }

                        if (currentEvent.address.isNotBlank()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.Info, null, tint = categoryColor, modifier = Modifier.size(18.dp).padding(top = 2.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = currentEvent.address,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // WEATHER SECTION
                        SectionHeader(title = "Etkinlik Günü Hava Durumu")
                        EventWeatherInfo(
                            uiState = weatherUiState,
                            eventDateStr = currentEvent.date,
                            findHourly = { weatherViewModel.getHourlyForEvent(it) }
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        SectionHeader(title = "Katılım Durumu")
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ModernStatusChip(
                                text = "İstiyorum",
                                isSelected = status == EventStatus.WANT_TO_GO,
                                onClick = { viewModel.setStatus(eventId, EventStatus.WANT_TO_GO) },
                                modifier = Modifier.weight(1f)
                            )
                            ModernStatusChip(
                                text = "Gideceğim",
                                isSelected = status == EventStatus.GOING,
                                onClick = { viewModel.setStatus(eventId, EventStatus.GOING) },
                                modifier = Modifier.weight(1f)
                            )
                            ModernStatusChip(
                                text = "Gittim",
                                isSelected = status == EventStatus.ATTENDED,
                                onClick = { viewModel.setStatus(eventId, EventStatus.ATTENDED) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        SectionHeader(title = "Hakkında")
                        Text(
                            text = currentEvent.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 26.sp
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                        SectionHeader(title = "Yorumlar")
                    }
                }

                if (comments.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("Henüz yorum yapılmamış.", color = MaterialTheme.colorScheme.outline)
                        }
                    }
                } else {
                    items(comments) { comment ->
                        CommentItem(comment)
                    }
                }

                item {
                    Column(modifier = Modifier.padding(24.dp)) {
                        OutlinedTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            placeholder = { Text("Düşüncelerini paylaş...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        )
                        Button(
                            onClick = {
                                if (commentText.isNotBlank()) {
                                    viewModel.addComment(eventId, commentText)
                                    commentText = ""
                                }
                            },
                            modifier = Modifier.align(Alignment.End).padding(top = 12.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = categoryColor)
                        ) {
                            Text("Yorum Yap")
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard(icon: ImageVector, label: String, value: String, accentColor: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null, tint = accentColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, maxLines = 2)
        }
    }
}

@Composable
fun CommentItem(comment: com.eskisehir.events.data.local.entity.CommentEntity) {
    val date = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(comment.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier.size(44.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(comment.userName.take(1).uppercase(), color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold)
            }
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(comment.userName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text(date, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(comment.content, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
