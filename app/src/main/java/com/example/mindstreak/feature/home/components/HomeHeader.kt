package com.example.mindstreak.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.theme.HabitOrange
import com.example.mindstreak.core.theme.HabitTeal

@Composable
fun HomeHeader(
    dateText: String,
    greetingText: String,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    notificationsContentDescription: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column {
            Text(
                text = dateText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )
            Text(
                text = greetingText,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 2.dp),
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp),
        ) {
            Box {
                FilledTonalIconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = notificationsContentDescription,
                        modifier = Modifier.size(17.dp),
                    )
                }
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-6).dp, y = 6.dp)
                        .clip(CircleShape)
                        .background(HabitOrange)
                        .border(2.dp, MaterialTheme.colorScheme.background, CircleShape),
                )
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(MaterialTheme.colorScheme.primary, HabitTeal)
                        )
                    )
                    .clickable(onClick = onProfileClick),
                contentAlignment = Alignment.Center,
            ) {
                Text("🧑‍💻", fontSize = 16.sp)
            }
        }
    }
}
