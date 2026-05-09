package com.eskisehir.eventapp.ui.screens.detail

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.eskisehir.eventapp.ui.components.CategoryFallbackBox
import com.eskisehir.eventapp.ui.components.EventImageUtils
import com.eskisehir.eventapp.ui.components.ModernStatusChip
import com.eskisehir.eventapp.ui.components.SectionHeader
import com.eskisehir.eventapp.ui.components.ShimmerPlaceholder
import com.eskisehir.events.presentation.components.EventLocationMapCard
import com.eskisehir.eventapp.ui.viewmodels.EventDetailViewModel
import com.eskisehir.eventapp.util.DateTimeUtils
import com.eskisehir.events.data.local.entity.EventStatus
import com.eskisehir.events.presentation.viewmodel.MapsViewModel
import com.eskisehir.events.presentation.viewmodel.RouteUiState
import com.eskisehir.events.presentation.components.TravelModeSelector
import com.eskisehir.events.presentation.components.RouteInfoCard
import com.eskisehir.events.util.LocationUtils
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: Long,
    onBackClick: () -> Unit,
    viewModel: EventDetailViewModel = hiltViewModel(),
    mapsViewModel: MapsViewModel = hiltViewModel()
) {
    val event by viewModel.event.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val status by viewModel.status.collectAsState()
    val mapsUiState by mapsViewModel.uiState.collectAsState()

    // Lazy load comments - only when needed
    var loadComments by remember { mutableStateOf(false) }
    val comments by if (loadComments) {
        viewModel.getComments(eventId).collectAsState(initial = emptyList())
    } else {
        remember { mutableStateOf(emptyList()) }
    }

    var commentText by remember { mutableStateOf("") }
    var showMap by remember { mutableStateOf(false) }
    var selectedTravelMode by remember { mutableStateOf("WALK") }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        mapsViewModel.onPermissionStatusChanged(isGranted)
    }

    // Check location permission when screen loads
    LaunchedEffect(Unit) {
        mapsViewModel.checkLocationPermission()
    }

    // Get user location and calculate routes
    LaunchedEffect(event) {
        event?.let { evt ->
            mapsViewModel.getUserLocation()
            // Once location is obtained, calculate all routes
            mapsViewModel.uiState.collect { state ->
                if (state.userLocation != null) {
                    mapsViewModel.calculateAllRoutes(
                        originLat = state.userLocation.latitude,
                        originLng = state.userLocation.longitude,
                        destLat = evt.latitude,
                        destLng = evt.longitude
                    )
                }
            }
        }
    }

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
                        modifier = Modifier.padding(8.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                        shadowElevation = 8.dp
                    ) {
                        IconButton(onClick = { showMap = !showMap }) {
                            Icon(Icons.Default.Map, contentDescription = "Harita")
                        }
                    }
                    Surface(
                        modifier = Modifier.padding(8.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                        shadowElevation = 8.dp
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
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
                    // Premium Image Header with gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(effectiveImageUrl)
                                .crossfade(300)
                                .allowHardware(false)
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
                        SectionHeader(title = "Konum")
                        Button(
                            onClick = { showMap = !showMap },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = categoryColor)
                        ) {
                            Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (showMap) "Haritayı Kapat" else "Haritayı Aç")
                        }

                        if (showMap) {
                            Spacer(modifier = Modifier.height(16.dp))

                            // Get current route duration and distance based on selected mode
                            val currentRoute = when (selectedTravelMode) {
                                "WALK" -> mapsUiState.walkRoute
                                "TRANSIT" -> mapsUiState.transitRoute
                                "DRIVE" -> mapsUiState.driveRoute
                                else -> mapsUiState.walkRoute
                            }
                            val routeDuration = if (currentRoute is RouteUiState.Success) currentRoute.durationSeconds else null
                            val routeDistance = if (currentRoute is RouteUiState.Success) currentRoute.distanceMeters else null

                            EventLocationMapCard(
                                title = currentEvent.name,
                                latitude = currentEvent.latitude,
                                longitude = currentEvent.longitude,
                                locationName = currentEvent.venue,
                                address = currentEvent.address,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 0.dp, vertical = 0.dp),
                                selectedTravelMode = selectedTravelMode,
                                duration = routeDuration,
                                distance = routeDistance,
                                userLocation = mapsUiState.userLocation
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                        SectionHeader(title = "Ulaşım")

                        // Location permission info
                        if (!mapsUiState.locationPermissionGranted) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Info,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            "Konum erişimi gerekli",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                    Button(
                                        onClick = { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
                                        modifier = Modifier
                                            .align(Alignment.End)
                                            .padding(top = 8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                    ) {
                                        Text("İzin Ver", color = Color.White)
                                    }
                                }
                            }
                        } else if (mapsUiState.userLocation != null) {
                            // Travel mode selector
                            TravelModeSelector(
                                selectedMode = selectedTravelMode,
                                onModeSelected = { selectedTravelMode = it }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Route info cards
                            RouteInfoCard(
                                travelMode = "WALK",
                                routeState = mapsUiState.walkRoute,
                                routeIcon = LocationUtils.getTravelModeIcon("WALK"),
                                isSelected = selectedTravelMode == "WALK",
                                onSelect = { selectedTravelMode = "WALK" },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            RouteInfoCard(
                                travelMode = "TRANSIT",
                                routeState = mapsUiState.transitRoute,
                                routeIcon = LocationUtils.getTravelModeIcon("TRANSIT"),
                                isSelected = selectedTravelMode == "TRANSIT",
                                onSelect = { selectedTravelMode = "TRANSIT" },
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                        SectionHeader(title = "Yorumlar")

                        // Load comments when user reaches this section
                        LaunchedEffect(Unit) {
                            loadComments = true
                        }
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
