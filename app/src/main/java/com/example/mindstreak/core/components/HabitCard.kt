package com.example.mindstreak.core.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.theme.HabitOrange
import kotlin.math.roundToInt
import androidx.core.graphics.toColorInt

data class HabitCardData(
    val id: String,
    val name: String,
    val emoji: String,
    val color: String,
    val streak: Int,
    val completionRate: Float,
    val completedToday: Boolean,
)

@Composable
fun HabitCard(
    modifier: Modifier = Modifier,
    habit: HabitCardData,
    onToggle: ((String) -> Unit)? = null,
    onClick: ((String) -> Unit)? = null,
) {
    val habitColor = remember(habit.color) { Color(habit.color.toColorInt()) }

    val cardBg by animateColorAsState(
        targetValue = if (habit.completedToday) MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface,
        animationSpec = tween(300),
        label = "cardBg",
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick(habit.id) } else Modifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // ── Emoji ─────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(habitColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) { Text(text = habit.emoji, fontSize = 18.sp) }
            // ── Info ──────────────────────────────────────────
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = habit.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (habit.completedToday) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (habit.completedToday) TextDecoration.LineThrough else TextDecoration.None,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "🔥 ${habit.streak}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = HabitOrange
                    )
                    Text(
                        text = "•",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    )
                    Text(
                        text = "${(habit.completionRate * 100).roundToInt()}% rate",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    )
                }
            }
            // ── Check button ──────────────────────────────────
            if (onToggle != null) {
                CheckButton(
                    completedToday = habit.completedToday,
                    onToggle = { onToggle(habit.id) },
                )
            }
        }
    }
}