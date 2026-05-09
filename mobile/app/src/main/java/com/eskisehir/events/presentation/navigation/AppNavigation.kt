package com.eskisehir.events.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eskisehir.events.presentation.screens.EventDetailScreen
import com.eskisehir.events.presentation.screens.RoadmapScreen
import java.time.LocalDate

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Ana Ekran - Test Butonları
        composable(Screen.Home.route) {
            TestHomeScreen(
                onEventClick = { eventId ->
                    navController.navigate("eventDetail/$eventId")
                },
                onRoadmapClick = {
                    navController.navigate(Screen.Roadmap.route)
                }
            )
        }

        // EventDetail Ekranı - Harita + Rota Hesaplama
        composable(Screen.EventDetail.route) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")?.toLongOrNull() ?: 1L

            EventDetailScreen(
                eventId = eventId,
                eventTitle = "Sanat ve Kültür Festivali",
                latitude = 39.7684,
                longitude = 30.5163,
                locationName = "Sazova Bilim Kültür Parkı",
                address = "Odunpazarı, Eskişehir",
                eventDate = LocalDate.now().toString() + "T20:00",
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Roadmap Ekranı - Multi-Stop Rota
        composable(Screen.Roadmap.route) {
            RoadmapScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

/**
 * Test ekranı - Yeni özellikleri görmek için
 */
@Composable
private fun TestHomeScreen(
    onEventClick: (Long) -> Unit = {},
    onRoadmapClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🗺️ Maps & Roadmap Demo",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Buton 1: Etkinlik Detayı
        ElevatedButton(
            onClick = { onEventClick(1) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📍 Etkinlik Detayını Aç\n(Harita + Rota)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buton 2: Roadmap
        ElevatedButton(
            onClick = onRoadmapClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🗺️ Roadmap'i Aç\n(Multi-Stop Rota)")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "💡 Butonlara tıkla ve yeni özellikleri test et!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
