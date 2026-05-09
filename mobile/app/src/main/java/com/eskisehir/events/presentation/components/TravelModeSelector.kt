package com.eskisehir.events.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Selector for travel mode (DRIVE, WALK, TRANSIT)
 */
@Composable
fun TravelModeSelector(
    selectedMode: String,
    onModeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val modes = listOf(
        Triple("DRIVE", Icons.Default.DirectionsCar, "Araba"),
        Triple("WALK", Icons.AutoMirrored.Filled.DirectionsWalk, "Yürüyüş"),
        Triple("TRANSIT", Icons.Default.DirectionsTransit, "Toplu Taşıma")
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Ulaşım Modu",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            modes.forEach { (mode, icon, label) ->
                FilterChip(
                    onClick = { onModeSelected(mode) },
                    label = { Text(label) },
                    leadingIcon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (selectedMode == mode)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    selected = selectedMode == mode,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Get icon for a travel mode
 */
fun getTravelModeIcon(mode: String): ImageVector = when (mode) {
    "DRIVE" -> Icons.Default.DirectionsCar
    "WALK" -> Icons.AutoMirrored.Filled.DirectionsWalk
    "TRANSIT" -> Icons.Default.DirectionsTransit
    else -> Icons.Default.DirectionsCar
}
