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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.eskisehir.eventapp.ui.components.ModernStatusChip
import com.eskisehir.eventapp.ui.components.SectionHeader
import com.eskisehir.eventapp.ui.viewmodels.EventDetailViewModel
import com.eskisehir.events.data.local.entity.EventStatus
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: Long, 
    onBackClick: () -> Unit, 
    onMapClick: ((Long) -> Unit)? = null,
    viewModel: EventDetailViewModel = hiltViewModel()
) {
    val event by viewModel.event.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val status by viewModel.status.collectAsState()
    val comments by viewModel.getComments(eventId).collectAsState(initial = emptyList())
    
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
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
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
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                    ) {
                        IconButton(onClick = { onMapClick?.invoke(eventId) }) {
                            Icon(Icons.Default.Map, contentDescription = "Harita")
                        }
                    }
                    Surface(
                        modifier = Modifier.padding(8.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
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
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    // Large Premium Image Header
                    AsyncImage(
                        model = event?.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                item {
                    Column(modifier = Modifier.padding(24.dp)) {
                        // Category & Badge
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = event?.category?.displayNameTr ?: "",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = event?.name ?: "",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Info Row Cards
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            InfoCard(Icons.Default.CalendarToday, "Tarih", event?.date ?: "", Modifier.weight(1f))
                            InfoCard(Icons.Default.Place, "Konum", event?.venue ?: "", Modifier.weight(1f))
                        }

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
                            text = event?.description ?: "", 
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 24.sp
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
                            shape = RoundedCornerShape(12.dp)
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
fun InfoCard(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, maxLines = 1)
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
