package com.eskisehir.eventapp.ui.screens.map

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.eskisehir.eventapp.data.model.SampleData
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(eventId: Long?, onEventClick: (Long) -> Unit, onBackClick: () -> Unit) {
    val events = if (eventId != null) {
        listOf(SampleData.events.find { it.id == eventId } ?: return)
    } else {
        SampleData.events
    }

    var showList by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Harita", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    TextButton(onClick = { showList = !showList }) {
                        Text(if (showList) "Harita" else "Liste")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        if (showList) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(events) { event ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        onClick = { onEventClick(event.id) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(event.name, fontWeight = FontWeight.Bold)
                                Text("${event.venue}", style = MaterialTheme.typography.bodySmall)
                                Text("📍 ${event.latitude}, ${event.longitude}",
                                    style = MaterialTheme.typography.labelSmall)
                            }
                            Icon(Icons.Default.LocationOn, contentDescription = null)
                        }
                    }
                }
            }
        } else {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    Configuration.getInstance().load(context, android.preference.PreferenceManager.getDefaultSharedPreferences(context))
                    MapView(context).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        controller.setZoom(11.0)
                        val centerLat = events.map { it.latitude }.average()
                        val centerLon = events.map { it.longitude }.average()
                        controller.setCenter(GeoPoint(centerLat, centerLon))

                        val items = events.map { event ->
                            OverlayItem(event.name, "${event.price.toInt()} ₺", GeoPoint(event.latitude, event.longitude))
                        }

                        val overlay = ItemizedIconOverlay(items, object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                            override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
                                val event = events[index]
                                onEventClick(event.id)
                                return true
                            }
                            override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean = false
                        }, context)
                        overlays.add(overlay)
                    }
                }
            )
        }
    }
}
