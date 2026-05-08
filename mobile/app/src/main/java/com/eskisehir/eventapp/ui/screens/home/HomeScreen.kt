package com.eskisehir.eventapp.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eskisehir.eventapp.data.model.Event
import com.eskisehir.eventapp.data.model.SampleData
import com.eskisehir.eventapp.ui.components.EventCard
import com.eskisehir.eventapp.ui.components.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onEventClick: (Long) -> Unit) {
    // We use sample data for immediate visual results
    val events = SampleData.events

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
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                SectionHeader(title = "Yaklaşan Etkinlikler")
            }
            
            items(events) { event ->
                EventCard(
                    event = event,
                    onClick = { onEventClick(event.id) }
                )
            }
        }
    }
}
