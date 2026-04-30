package com.example.mindstreak.feature.habit_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.components.StatItem

@Composable
fun HabitHeroCard(
    name: String,
    emoji: String,
    category: String,
    frequency: String,
    reminderTime: String,
    streak: String,
    bestStreak: String,
    completionRate: String,
    habitColor: Color,
    statsLabels: List<String>, // [Current, Best, Rate]
    reminderTemplate: String, // e.g. "⏰ %s at %s"
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            habitColor.copy(alpha = 0.15f),
                            habitColor.copy(alpha = 0.05f)
                        )
                    )
                )
                .border(1.dp, habitColor.copy(alpha = 0.2f), RoundedCornerShape(28.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(habitColor.copy(alpha = 0.12f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(emoji, fontSize = 32.sp)
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            category.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            letterSpacing = 1.sp
                        )
                        Text(name, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                        Text(
                            text = String.format(reminderTemplate, frequency, reminderTime),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatItem(
                        Modifier.weight(1f),
                        streak,
                        statsLabels.getOrElse(0) { "" },
                        habitColor
                    )
                    StatItem(
                        Modifier.weight(1f),
                        bestStreak,
                        statsLabels.getOrElse(1) { "" },
                        com.example.mindstreak.core.theme.HabitOrange
                    )
                    StatItem(
                        Modifier.weight(1f),
                        completionRate,
                        statsLabels.getOrElse(2) { "" },
                        com.example.mindstreak.core.theme.HabitTeal
                    )
                }
            }
        }
    }
}
