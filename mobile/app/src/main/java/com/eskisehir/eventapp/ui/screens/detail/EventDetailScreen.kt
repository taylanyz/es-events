package com.eskisehir.eventapp.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.eskisehir.eventapp.data.model.Event
import com.eskisehir.events.data.remote.api.EventApiService
import com.eskisehir.events.data.remote.dto.InteractionRequestDto
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.eskisehir.eventapp.data.model.SampleData

/**
 * Event Detail Screen - Shows full event information.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(eventId: Long, onBackClick: () -> Unit, onMapClick: ((Long) -> Unit)? = null) {
    val event = remember { mutableStateOf<Event?>(null) }
    val isFavorite = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val api = remember { mutableStateOf<EventApiService?>(null) }

    LaunchedEffect(Unit) {
        try {
            val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
            val client = OkHttpClient.Builder().addInterceptor(logging).build()
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8081/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            api.value = retrofit.create(EventApiService::class.java)
            val eventDto = api.value!!.getEventById(eventId)
            event.value = Event(
                id = eventDto.id,
                name = eventDto.name,
                description = eventDto.description,
                category = com.eskisehir.eventapp.data.model.Category.valueOf(eventDto.category),
                latitude = eventDto.latitude,
                longitude = eventDto.longitude,
                venue = eventDto.venue,
                date = eventDto.date,
                price = eventDto.price,
                imageUrl = eventDto.imageUrl ?: "",
                tags = eventDto.tags ?: emptyList()
            )
        } catch (e: Exception) {
            android.util.Log.e("EventDetailScreen", "Failed to fetch event: ${e.message}")
            event.value = SampleData.events.find { it.id == eventId }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(event.value?.name ?: "Etkinlik") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onMapClick?.invoke(eventId) }
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Haritada Göster")
                    }
                    IconButton(
                        onClick = {
                            isFavorite.value = !isFavorite.value
                            scope.launch {
                                try {
                                    api.value?.logInteraction(InteractionRequestDto(eventId, true))
                                } catch (e: Exception) {
                                    android.util.Log.e("EventDetailScreen", "Failed to log favorite: ${e.message}")
                                }
                            }
                        }
                    ) {
                        Icon(
                            if (isFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorilere Ekle",
                            tint = if (isFavorite.value) MaterialTheme.colorScheme.error else LocalContentColor.current
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        if (event.value == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Etkinlik bulunamadı")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Event image
            AsyncImage(
                model = event.value?.imageUrl,
                contentDescription = event.value?.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                // Category
                SuggestionChip(
                    onClick = {},
                    label = { Text(event.value?.category?.displayNameTr ?: "Kategori") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Title
                Text(
                    text = event.value?.name ?: "",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Info rows
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(event.value?.venue ?: "", style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(event.value?.date ?: "", style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AttachMoney, contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (event.value?.price == 0.0) "Ücretsiz" else "${event.value?.price?.toInt()} ₺",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = "Açıklama",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = event.value?.description ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tags
                Text(
                    text = "Etiketler",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    event.value?.tags?.forEach { tag ->
                        AssistChip(
                            onClick = {},
                            label = { Text(tag) }
                        )
                    }
                }
            }
        }
    }
}
