package com.eskisehir.eventapp.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eskisehir.eventapp.data.model.Event
import com.eskisehir.eventapp.data.model.SampleData
import com.eskisehir.eventapp.ui.components.EventCard
import com.eskisehir.events.data.remote.api.EventApiService
import com.eskisehir.events.data.remote.dto.InteractionRequestDto
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onEventClick: (Long) -> Unit) {
    var events by remember { mutableStateOf<List<Event>>(SampleData.events) }
    var errorMsg by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var api by remember { mutableStateOf<EventApiService?>(null) }

    LaunchedEffect(Unit) {
        try {
            val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
            val client = OkHttpClient.Builder().addInterceptor(logging).build()
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8081/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            api = retrofit.create(EventApiService::class.java)
            val apiEvents = api!!.getEvents()
            android.util.Log.d("HomeScreen", "Got ${apiEvents.size} events from API")

            events = apiEvents.map { dto ->
                Event(
                    id = dto.id,
                    name = dto.name,
                    description = dto.description,
                    category = com.eskisehir.eventapp.data.model.Category.valueOf(dto.category),
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
            android.util.Log.e("HomeScreen", "API Error: ${e.message}", e)
            errorMsg = "Error: ${e.message}"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Eskişehir Events", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(events) { event ->
                EventCard(
                    event = event,
                    onClick = {
                        scope.launch {
                            try {
                                api?.logInteraction(InteractionRequestDto(event.id, true))
                                android.util.Log.d("HomeScreen", "Interaction logged for event ${event.id}")
                            } catch (e: Exception) {
                                android.util.Log.e("HomeScreen", "Failed to log interaction: ${e.message}")
                            }
                        }
                        onEventClick(event.id)
                    }
                )
            }
        }
    }
}
