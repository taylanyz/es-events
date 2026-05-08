package com.eskisehir.eventapp.ui.screens.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eskisehir.eventapp.ui.components.EmptyState
import com.eskisehir.eventapp.ui.components.EventCard
import com.eskisehir.eventapp.ui.components.SectionHeader
import com.eskisehir.eventapp.ui.viewmodels.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onEventClick: (Long) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favoriteEvents by viewModel.favoriteEvents.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Favorilerim", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        if (isLoading && favoriteEvents.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (favoriteEvents.isEmpty()) {
            EmptyState(
                modifier = Modifier.padding(padding),
                icon = Icons.Default.FavoriteBorder,
                message = "Henüz favori etkinlik eklemediniz.\nBeğendiğiniz etkinlikleri favorilere ekleyerek burada görebilirsiniz."
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    SectionHeader(title = "Kaydedilen Etkinlikler")
                }
                items(favoriteEvents) { event ->
                    EventCard(
                        event = event,
                        onClick = { onEventClick(event.id) }
                    )
                }
            }
        }
    }
}
