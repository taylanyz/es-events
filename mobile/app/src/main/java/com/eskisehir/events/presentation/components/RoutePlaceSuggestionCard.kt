package com.eskisehir.events.presentation.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eskisehir.eventapp.data.model.RoutePlaceSuggestion
import java.util.Locale

@Composable
fun RoutePlaceSuggestionCard(
    suggestion: RoutePlaceSuggestion,
    isAlreadyInRoute: Boolean = false,
    onShowOnMap: () -> Unit,
    onAddToRoute: () -> Unit
) {
    val context = LocalContext.current

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            // Header with Gradient Background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                if (isAlreadyInRoute) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                                else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                                if (isAlreadyInRoute) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        val icon = when {
                            suggestion.primaryType?.contains("restaurant") == true -> "🍴"
                            suggestion.primaryType?.contains("cafe") == true -> "☕"
                            suggestion.primaryType?.contains("bakery") == true -> "🥐"
                            suggestion.primaryType?.contains("coffee") == true -> "☕"
                            suggestion.primaryType?.contains("dessert") == true -> "🍰"
                            else -> "📍"
                        }
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(icon, fontSize = 22.sp)
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = suggestion.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            color = if (isAlreadyInRoute) MaterialTheme.colorScheme.onPrimaryContainer 
                                    else MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        val active = (suggestion.rating ?: 0.0) >= (index + 1)
                        Icon(
                            Icons.Default.Star, 
                            null, 
                            tint = if (active) Color(0xFFFFB300) else Color.LightGray, 
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "${suggestion.rating} (${suggestion.userRatingCount} yorum)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(8.dp))
                
                Text(
                    text = suggestion.primaryType?.replace("_", " ")?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } ?: "Mola Noktası",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                
                Text(
                    text = suggestion.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 2,
                    lineHeight = 16.sp
                )

                Spacer(Modifier.height(16.dp))

                // Personalized Reason Box
                Surface(
                    color = if (isAlreadyInRoute) MaterialTheme.colorScheme.primary.copy(alpha = 0.06f)
                            else MaterialTheme.colorScheme.secondary.copy(alpha = 0.06f),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, 
                        (if (isAlreadyInRoute) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary).copy(alpha = 0.1f)
                    )
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                        Text(if (isAlreadyInRoute) "✅" else "✨", fontSize = 16.sp)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (isAlreadyInRoute) "Bu mekan rotana başarıyla eklendi." else suggestion.reason,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onShowOnMap,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Icon(Icons.Default.Place, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Harita", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onAddToRoute,
                        enabled = !isAlreadyInRoute,
                        modifier = Modifier.weight(1.3f).height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isAlreadyInRoute) MaterialTheme.colorScheme.surfaceVariant 
                                             else MaterialTheme.colorScheme.primary
                        ),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        Icon(
                            if (isAlreadyInRoute) Icons.Default.Check else Icons.Default.AddLocationAlt, 
                            null, 
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            if (isAlreadyInRoute) "Rotada" else "Rotaya Ekle", 
                            fontSize = 12.sp, 
                            fontWeight = FontWeight.Bold
                        )
                    }

                    FilledTonalIconButton(
                        onClick = {
                            val uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${suggestion.latitude},${suggestion.longitude}&query_place_id=${suggestion.id}")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            context.startActivity(intent)
                        },
                        modifier = Modifier.size(44.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Map, null, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}
