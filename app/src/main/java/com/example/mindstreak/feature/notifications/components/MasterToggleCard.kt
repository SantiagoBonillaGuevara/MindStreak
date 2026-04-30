package com.example.mindstreak.feature.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.components.CustomToggle
import com.example.mindstreak.core.theme.HabitPurple

@Composable
fun MasterToggleCard(
    isOn: Boolean,
    onToggle: (Boolean) -> Unit,
    title: String,
    activeSubtitle: String,
    inactiveSubtitle: String,
    modifier: Modifier = Modifier,
) {
    val bg = if (isOn) HabitPurple.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
    val borderColor = if (isOn) HabitPurple.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outlineVariant

    Row(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(bg)
            .border(1.5.dp, borderColor, RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(40.dp).background(if(isOn) HabitPurple.copy(alpha = 0.2f) else MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                if(isOn) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                contentDescription = null,
                tint = if(isOn) HabitPurple else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(Modifier.weight(1f)) {
            Text(title, color = MaterialTheme.colorScheme.onSurface, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(if(isOn) activeSubtitle else inactiveSubtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
        CustomToggle(checked = isOn, onCheckedChange = onToggle)
    }
}
