package com.eskisehir.events.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eskisehir.events.data.local.entity.RoadmapStopEntity

/**
 * Card displaying a single roadmap stop with order management
 */
@Composable
fun RoadmapStopCard(
    stop: RoadmapStopEntity,
    index: Int,
    isFirst: Boolean,
    isLast: Boolean,
    onRemove: (Long) -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSuggested = stop.eventId < 0

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isSuggested) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f) 
                             else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Index Circle
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        if (isSuggested) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary, 
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (index + 1).toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(16.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                if (isSuggested) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = "YOL ÜSTÜ DURAK",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
                
                Text(
                    text = stop.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Place,
                        null,
                        modifier = Modifier.size(14.dp),
                        tint = if (isSuggested) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = stop.locationName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Order Controls
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onMoveUp, enabled = !isFirst, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.KeyboardArrowUp, 
                        "Yukarı taşı", 
                        tint = if (isFirst) Color.LightGray else (if (isSuggested) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary)
                    )
                }
                IconButton(onClick = onMoveDown, enabled = !isLast, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.KeyboardArrowDown, 
                        "Aşağı taşı", 
                        tint = if (isLast) Color.LightGray else (if (isSuggested) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary)
                    )
                }
            }

            // Remove
            IconButton(onClick = { onRemove(stop.eventId) }, modifier = Modifier.size(40.dp)) {
                Icon(Icons.Default.Delete, "Sil", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
