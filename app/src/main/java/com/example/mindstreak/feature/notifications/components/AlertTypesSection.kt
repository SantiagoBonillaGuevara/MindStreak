package com.example.mindstreak.feature.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.components.NotificationTypeRow

@Composable
fun AlertTypesSection(
    isMasterOn: Boolean,
    sectionTitle: String,
    types: List<NotificationTypeData>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .alpha(if (isMasterOn) 1f else 0.4f)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                sectionTitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        types.forEach { type ->
            NotificationTypeRow(
                icon = type.icon,
                label = type.title,      // Corregido: 'title' -> 'label'
                sub = type.subtitle,     // Corregido: 'subtitle' -> 'sub'
                color = type.color,
                value = type.checked,     // Corregido: 'checked' -> 'value'
                onValueChange = type.onCheckedChange, // Corregido: 'onCheckedChange' -> 'onValueChange'
                masterEnabled = isMasterOn // Corregido: 'enabled' -> 'masterEnabled'
            )
        }
    }
}

data class NotificationTypeData(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val color: androidx.compose.ui.graphics.Color,
    val checked: Boolean,
    val onCheckedChange: (Boolean) -> Unit,
)
