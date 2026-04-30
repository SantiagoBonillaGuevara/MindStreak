package com.example.mindstreak.feature.streak.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.theme.HabitTeal

@Composable
fun MilestoneItem(
    emoji: String,
    label: String,
    days: Int,
    isAchieved: Boolean,
    isCurrent: Boolean,
    currentStreak: Int,
    color: Color,
    daysLabel: String,
    remainingTemplate: String, // e.g. "%dd"
    modifier: Modifier = Modifier
) {
    val bgColor =
        if (isCurrent) color.copy(alpha = 0.1f) else MaterialTheme.colorScheme.secondary.copy(alpha = if (isAchieved) 0.3f else 0.1f)
    val borderColor =
        if (isCurrent) color.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)

    Row(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(emoji, fontSize = 24.sp, modifier = Modifier.alpha(if (isAchieved) 1f else 0.5f))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isAchieved) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.5f
                )
            )
            Text(
                "$days $daysLabel",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        if (isAchieved) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(if (isCurrent) color else HabitTeal, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("✓", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black)
            }
        } else {
            Text(
                String.format(remainingTemplate, days - currentStreak),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}
