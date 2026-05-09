package com.eskisehir.eventapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.eskisehir.eventapp.data.model.Event
import com.eskisehir.eventapp.ui.components.EmptyState
import com.eskisehir.eventapp.ui.components.ProfileStatItem
import com.eskisehir.eventapp.ui.components.SectionHeader
import com.eskisehir.eventapp.util.DateTimeUtils
import com.eskisehir.eventapp.ui.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    onEditProfileClick: () -> Unit,
    onEventClick: (Long) -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profile by viewModel.userProfile.collectAsState()
    val places by viewModel.favoritePlaces.collectAsState()
    val attended by viewModel.attendedEvents.collectAsState()
    val going by viewModel.goingEvents.collectAsState()
    val wantToGo by viewModel.wantToGoEvents.collectAsState()
    val favorites by viewModel.favoriteEvents.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val tabs = listOf("Favoriler", "Gideceğim", "İstiyorum", "Gittim")

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Çıkış Yap") },
            text = { Text("Hesabınızdan çıkış yapmak istediğinize emin misiniz?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    viewModel.logout()
                    onLogoutClick()
                }) {
                    Text("Evet", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Hayır")
                }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profilim", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold) },
                actions = {
                    IconButton(onClick = onEditProfileClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Ayarlar", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout, 
                            contentDescription = "Çıkış Yap", 
                            tint = Color.Red
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Header Section
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(120.dp), contentAlignment = Alignment.BottomEnd) {
                        if (profile?.profileImageUri != null) {
                            AsyncImage(
                                model = profile?.profileImageUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize().clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (profile?.fullName ?: profile?.email ?: "U").take(1).uppercase(),
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        profile?.fullName ?: "İsim belirtilmemiş",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        profile?.email ?: "kullanici@email.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Stats Row
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProfileStatItem("Favori", favorites.size.toString())
                            VerticalDivider(modifier = Modifier.height(40.dp).width(1.dp))
                            ProfileStatItem("Gidilecek", going.size.toString())
                            VerticalDivider(modifier = Modifier.height(40.dp).width(1.dp))
                            ProfileStatItem("Gidildi", attended.size.toString())
                        }
                    }
                }
            }

            // Interests Section
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    SectionHeader(title = "İlgi Alanlarım")
                    if (profile?.interests.isNullOrEmpty()) {
                        Text("Henüz ilgi alanı seçilmedi.", color = MaterialTheme.colorScheme.outline, style = MaterialTheme.typography.bodyMedium)
                    } else {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            profile?.interests?.forEach { interest ->
                                SuggestionChip(
                                    onClick = {}, 
                                    label = { Text(interest) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Tabs for Events
            item {
                Spacer(modifier = Modifier.height(32.dp))
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    edgePadding = 24.dp,
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            height = 3.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { 
                                Text(
                                    title, 
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium
                                ) 
                            }
                        )
                    }
                }
            }

            // Event List based on tab
            val currentList = when(selectedTab) {
                0 -> favorites
                1 -> going
                2 -> wantToGo
                else -> attended
            }

            if (currentList.isEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.AutoMirrored.Filled.EventNote,
                        message = "Henüz bu kategoride etkinlik bulunmuyor."
                    )
                }
            } else {
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(currentList) { event ->
                            ProfileEventCard(event) { onEventClick(event.id) }
                        }
                    }
                }
            }

            // Favorite Places
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Spacer(modifier = Modifier.height(16.dp))
                    SectionHeader(title = "Favori Mekanlarım")
                    if (places.isEmpty()) {
                        Text("Henüz favori mekan eklemediniz.", color = MaterialTheme.colorScheme.outline)
                    }
                }
            }

            items(places) { place ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(place.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(place.location, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileEventCard(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier.width(220.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(120.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(event.name, maxLines = 1, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold)
                Text(
                    text = DateTimeUtils.formatEventDateShort(event.date),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(event.venue, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline, maxLines = 1)
                }
            }
        }
    }
}
