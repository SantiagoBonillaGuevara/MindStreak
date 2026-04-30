package com.example.mindstreak.feature.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.components.CustomToggle
import androidx.core.graphics.toColorInt

@Composable
fun HabitReminderItem(
    emoji: String,
    name: String,
    timeText: String,
    frequencyText: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    activeColorHex: String,
    isMasterOn: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .alpha(if (isMasterOn) 1f else 0.4f)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(emoji, fontSize = 20.sp)
        Column(Modifier.weight(1f)) {
            Text(
                name,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    Icons.Default.AccessTime,
                    null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(10.dp)
                )
                Text(
                    "$timeText · $frequencyText",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
        }
        CustomToggle(
            checked = isEnabled,
            onCheckedChange = { if (isMasterOn) onToggle(it) },
            activeColor = try {
                Color(activeColorHex.toColorInt())
            } catch (_: Exception) {
                MaterialTheme.colorScheme.primary
            }
        )
    }
}
