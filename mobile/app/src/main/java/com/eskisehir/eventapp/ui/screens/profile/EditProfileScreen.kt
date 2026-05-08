package com.eskisehir.eventapp.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
import com.eskisehir.eventapp.ui.components.ModernButton
import com.eskisehir.eventapp.ui.components.SectionHeader
import com.eskisehir.eventapp.ui.viewmodels.EditProfileViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val profile by viewModel.userProfile.collectAsState()
    
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<String?>(null) }
    var selectedInterests by remember { mutableStateOf(setOf<String>()) }
    
    val allInterests = listOf("Müzik", "Spor", "Sanat", "Tiyatro", "Sinema", "Teknoloji", "Eğitim", "Yemek", "Festival", "Gezi", "Kültür", "Workshop")

    LaunchedEffect(profile) {
        profile?.let {
            fullName = it.fullName ?: ""
            email = it.email
            profileImageUri = it.profileImageUri
            selectedInterests = it.interests.toSet()
        }
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        profileImageUri = uri?.toString()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profili Düzenle", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
                },
                actions = {
                    TextButton(onClick = {
                        viewModel.saveProfile(fullName, email.ifBlank { "kullanici@email.com" }, profileImageUri, selectedInterests.toList())
                        onBackClick()
                    }) {
                        Text("Kaydet", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image Edit
            Box(
                modifier = Modifier.size(130.dp).clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.BottomEnd
            ) {
                if (profileImageUri != null) {
                    AsyncImage(
                        model = profileImageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer, CircleShape), 
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                    modifier = Modifier.size(36.dp).offset(x = (-4).dp, y = (-4).dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            SectionHeader(title = "Kişisel Bilgiler")
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Ad Soyad") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-posta") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            SectionHeader(title = "İlgi Alanları")
            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                allInterests.forEach { interest ->
                    val isSelected = selectedInterests.contains(interest)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedInterests = if (isSelected) selectedInterests - interest else selectedInterests + interest
                        },
                        label = { Text(interest) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            SectionHeader(title = "Favori Mekan Ekle")
            var placeName by remember { mutableStateOf("") }
            var placeLoc by remember { mutableStateOf("") }
            
            OutlinedTextField(
                value = placeName, 
                onValueChange = { placeName = it }, 
                label = { Text("Mekan Adı") }, 
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = placeLoc, 
                onValueChange = { placeLoc = it }, 
                label = { Text("Konum/Adres") }, 
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            ModernButton(
                text = "Mekan Ekle", 
                onClick = {
                    if (placeName.isNotBlank() && placeLoc.isNotBlank()) {
                        viewModel.addFavoritePlace(placeName, placeLoc, null)
                        placeName = ""
                        placeLoc = ""
                    }
                },
                containerColor = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
