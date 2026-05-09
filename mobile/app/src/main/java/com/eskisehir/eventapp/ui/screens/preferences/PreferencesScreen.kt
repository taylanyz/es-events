package com.eskisehir.eventapp.ui.screens.preferences

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eskisehir.eventapp.data.model.Event
import com.eskisehir.events.data.remote.api.EventApiService
import com.eskisehir.events.data.remote.dto.SmartRecommendationRequestDto
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(onEventClick: (Long) -> Unit) {
    var api by remember { mutableStateOf<EventApiService?>(null) }

    // Budget
    var maxPrice by remember { mutableStateOf(100.0) }
    var minPrice by remember { mutableStateOf(0.0) }

    // Duration
    var minDuration by remember { mutableStateOf(30) }
    var maxDuration by remember { mutableStateOf(240) }

    // Environment
    var selectedEnvironment by remember { mutableStateOf<String?>(null) }
    var selectedCrowdSize by remember { mutableStateOf<String?>(null) }
    var isIndoor by remember { mutableStateOf<Boolean?>(null) }

    // Activity
    var selectedActivityLevel by remember { mutableStateOf<String?>(null) }
    var selectedDifficulty by remember { mutableStateOf<String?>(null) }

    // Social
    var selectedSocialAspect by remember { mutableStateOf<String?>(null) }
    var selectedGroupSize by remember { mutableStateOf<String?>(null) }

    // Culture & Age
    var selectedAgeGroup by remember { mutableStateOf<String?>(null) }
    var selectedCulturalValue by remember { mutableStateOf<String?>(null) }

    // Time & Weather
    var selectedTimeOfDay by remember { mutableStateOf<String?>(null) }
    var weatherDependent by remember { mutableStateOf<Boolean?>(null) }

    // Accessibility
    var requireWheelchair by remember { mutableStateOf(false) }
    var requireParking by remember { mutableStateOf(false) }
    var requireTransport by remember { mutableStateOf(false) }
    var requirePhotography by remember { mutableStateOf(false) }
    var requireFood by remember { mutableStateOf(false) }

    // Results
    var recommendations by remember { mutableStateOf<List<Event>>(emptyList()) }
    var recommendationDetails by remember { mutableStateOf<Map<Long, Pair<String, Double>>>(emptyMap()) }
    var rawGeminiOutput by remember { mutableStateOf("") }
    var showRawOutput by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

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
        } catch (e: Exception) {
            android.util.Log.e("PreferencesScreen", "API init error: ${e.message}")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tercihlerinizi Belirtin", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Bütçe", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Column {
                    Text("Max Fiyat: %.0f₺".format(maxPrice), style = MaterialTheme.typography.labelSmall)
                    Slider(value = maxPrice.toFloat(), onValueChange = { maxPrice = it.toDouble() },
                        valueRange = 0f..300f, modifier = Modifier.fillMaxWidth())

                    Text("Min Fiyat: %.0f₺".format(minPrice), style = MaterialTheme.typography.labelSmall)
                    Slider(value = minPrice.toFloat(), onValueChange = { minPrice = it.toDouble() },
                        valueRange = 0f..300f, modifier = Modifier.fillMaxWidth())
                }
            }

            item {
                Text("Süre (dakika)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Column {
                    Text("Min: %d dk".format(minDuration), style = MaterialTheme.typography.labelSmall)
                    Slider(value = minDuration.toFloat(), onValueChange = { minDuration = it.toInt() },
                        valueRange = 0f..480f, modifier = Modifier.fillMaxWidth())

                    Text("Max: %d dk".format(maxDuration), style = MaterialTheme.typography.labelSmall)
                    Slider(value = maxDuration.toFloat(), onValueChange = { maxDuration = it.toInt() },
                        valueRange = 0f..480f, modifier = Modifier.fillMaxWidth())
                }
            }

            item { SelectDropdown("Ortam Tipi", listOf("kalabalık", "sessiz", "karma"), selectedEnvironment) { selectedEnvironment = it } }
            item { SelectDropdown("Kalabalık Seviyesi", listOf("Çok az", "Az", "Orta", "Çok"), selectedCrowdSize) { selectedCrowdSize = it } }

            item {
                Text("Kapalı Hava", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { isIndoor = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isIndoor == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) { Text("Evet") }
                    Button(
                        onClick = { isIndoor = false },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isIndoor == false) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) { Text("Hayır") }
                    Button(
                        onClick = { isIndoor = null },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors()
                    ) { Text("Farketmez") }
                }
            }

            item { SelectDropdown("Aktivite Seviyesi", listOf("pasif", "hafif", "orta", "yoğun"), selectedActivityLevel) { selectedActivityLevel = it } }
            item { SelectDropdown("Zorluk Seviyesi", listOf("başlangıç", "orta", "ileri"), selectedDifficulty) { selectedDifficulty = it } }

            item { SelectDropdown("Sosyal Aspekt", listOf("yalnız", "arkadaş", "yeni insanlar"), selectedSocialAspect) { selectedSocialAspect = it } }
            item { SelectDropdown("Grup Boyutu", listOf("solo", "çift", "grup", "her biri"), selectedGroupSize) { selectedGroupSize = it } }

            item { SelectDropdown("Yaş Grubu", listOf("Çocuk", "Genç", "Yetişkin", "Yaşlı"), selectedAgeGroup) { selectedAgeGroup = it } }
            item { SelectDropdown("Kültürel Değer", listOf("Hiç", "Biraz", "Çok"), selectedCulturalValue) { selectedCulturalValue = it } }

            item { SelectDropdown("Zaman Tercihi", listOf("Sabah", "Öğle", "Akşam", "Gece"), selectedTimeOfDay) { selectedTimeOfDay = it } }

            item {
                Text("Hava-Bağımlı", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { weatherDependent = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (weatherDependent == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) { Text("Evet") }
                    Button(
                        onClick = { weatherDependent = false },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (weatherDependent == false) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) { Text("Hayır") }
                    Button(
                        onClick = { weatherDependent = null },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors()
                    ) { Text("Farketmez") }
                }
            }

            item {
                Text("Erişilebilirlik Gereksinimleri", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CheckboxItem("Tekerlekli Sandalye Erişimi", requireWheelchair) { requireWheelchair = it }
                    CheckboxItem("Otopark", requireParking) { requireParking = it }
                    CheckboxItem("Toplu Taşıma", requireTransport) { requireTransport = it }
                    CheckboxItem("Fotoğraf Çekme", requirePhotography) { requirePhotography = it }
                    CheckboxItem("Yemek/İçecek", requireFood) { requireFood = it }
                }
            }

            item {
                Button(
                    onClick = {
                        isLoading = true
                        errorMsg = ""
                        scope.launch {
                            try {
                                val request = SmartRecommendationRequestDto(
                                    userId = 1L,
                                    limit = 5,
                                    maxPrice = maxPrice,
                                    minPrice = minPrice,
                                    minDuration = minDuration,
                                    maxDuration = maxDuration,
                                    environmentType = selectedEnvironment,
                                    crowdSize = selectedCrowdSize,
                                    isIndoor = isIndoor,
                                    activityLevel = selectedActivityLevel,
                                    difficultyLevel = selectedDifficulty,
                                    socialAspect = selectedSocialAspect,
                                    groupSize = selectedGroupSize,
                                    ageGroup = selectedAgeGroup,
                                    culturalValue = selectedCulturalValue,
                                    bestTimeOfDay = selectedTimeOfDay,
                                    weatherDependent = weatherDependent,
                                    requireWheelchair = if (requireWheelchair) true else null,
                                    requireParking = if (requireParking) true else null,
                                    requireTransport = if (requireTransport) true else null,
                                    requirePhotography = if (requirePhotography) true else null,
                                    requireFood = if (requireFood) true else null
                                )

                                val response = api?.getSmartRecommendations(request) ?: emptyList()

                                val details = mutableMapOf<Long, Pair<String, Double>>()
                                val outputLog = StringBuilder("=== GEMINI ÇIKTISI ===\n\n")

                                response.forEach { dto ->
                                    details[dto.eventId] = Pair(dto.explanation, dto.finalScore)
                                    outputLog.append("📍 ${dto.eventName}\n")
                                    outputLog.append("   Skor: ${(dto.finalScore * 100).toInt()}%\n")
                                    outputLog.append("   Açıklama: ${dto.explanation}\n\n")
                                }

                                rawGeminiOutput = outputLog.toString()
                                recommendationDetails = details

                                android.util.Log.d("PreferencesScreen", "Raw response: $response")
                                android.util.Log.d("PreferencesScreen", "Response size: ${response.size}")

                                recommendations = response.map { dto ->
                                    Event(
                                        id = dto.eventId,
                                        name = dto.eventName,
                                        description = "",
                                        category = com.eskisehir.eventapp.data.model.Category.OTHER,
                                        latitude = 0.0,
                                        longitude = 0.0,
                                        venue = "",
                                        date = "",
                                        price = 0.0,
                                        imageUrl = "",
                                        tags = dto.tags
                                    )
                                }

                                android.util.Log.d("PreferencesScreen", "Got ${recommendations.size} recommendations")
                            } catch (e: Exception) {
                                android.util.Log.e("PreferencesScreen", "Error: ${e.message}")
                                errorMsg = "Hata: ${e.message}"
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Aranıyor...")
                    } else {
                        Text("Önerilen Etkinlikleri Bul")
                    }
                }
            }

            if (errorMsg.isNotEmpty()) {
                item {
                    Text(errorMsg, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }

            if (recommendations.isNotEmpty()) {
                item {
                    Button(
                        onClick = { showRawOutput = !showRawOutput },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors()
                    ) {
                        Text(if (showRawOutput) "Çıktıyı Gizle" else "Gemini Çıktısını Göster")
                    }
                }

                if (showRawOutput && rawGeminiOutput.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text(
                                rawGeminiOutput,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("✨ Sizin için Öneriler", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }

                items(recommendations.size) { index ->
                    val event = recommendations[index]
                    val (explanation, score) = recommendationDetails[event.id] ?: Pair("", 0.0)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onEventClick(event.id) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(event.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                                Text("${(score * 100).toInt()}%", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(explanation, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(6.dp))
                            if (event.tags != null && event.tags.isNotEmpty()) {
                                Text(event.tags.joinToString(", "), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectDropdown(label: String, options: List<String>, selected: String?, onSelected: (String?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors()
        ) {
            Text(selected ?: "Seç...", modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Start)
            if (selected != null) {
                Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.error)
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { onSelected(option); expanded = false }
                )
            }
            if (selected != null) {
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text("Temizle", color = MaterialTheme.colorScheme.error) },
                    onClick = { onSelected(null); expanded = false }
                )
            }
        }
    }
}

@Composable
fun CheckboxItem(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(label, modifier = Modifier.padding(start = 8.dp))
    }
}
