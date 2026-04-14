package com.example.mindstreak.core.components

import android.graphics.Color.parseColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// Equivalente a HabitCardProps
data class HabitCardData(
    val id: String,
    val name: String,
    val emoji: String,
    val color: String,           // hex string "#FF6B35"
    val streak: Int,
    val completionRate: Float,
    val completedToday: Boolean,
)

@Composable
fun HabitCard(
    habit: HabitCardData,
    onToggle: ((String) -> Unit)? = null,
    onClick: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val habitColor = remember(habit.color) {
        Color(parseColor(habit.color))
    }

    // Equivalente a bg-primary/5 vs bg-card según completedToday
    val cardBg by animateColorAsState(
        targetValue = if (habit.completedToday)
            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
        else
            MaterialTheme.colorScheme.surface,
        animationSpec = tween(300),
        label = "cardBg",
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null)
                    Modifier.clickable { onClick(habit.id) }
                else Modifier
            ),
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
            // Equivalente al div con background: `${color}20`
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(habitColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = habit.emoji, fontSize = 18.sp)
            }

            // ── Info ──────────────────────────────────────────
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = habit.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (habit.completedToday)
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    else
                        MaterialTheme.colorScheme.onSurface,
                    // Equivalente a line-through cuando completedToday
                    textDecoration = if (habit.completedToday)
                        TextDecoration.LineThrough
                    else
                        TextDecoration.None,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "🔥 ${habit.streak}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFF6B35),
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

@Composable
private fun CheckButton(
    completedToday: Boolean,
    onToggle: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    // Equivalente a whileTap={{ scale: 0.85 }} y animate isTapped
    val scale = remember { Animatable(1f) }

    // Colores del botón según estado
    val completedGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF2DD4BF), Color(0xFF14B8A6)) // teal-400 → teal-500
    )

    Box(
        modifier = Modifier
            .size(32.dp)
            .scale(scale.value)
            .clip(CircleShape)
            .background(
                if (completedToday) Color.Transparent
                else MaterialTheme.colorScheme.secondary
            )
            .then(
                if (completedToday)
                    Modifier.background(brush = completedGradient, shape = CircleShape)
                else
                    Modifier
            )
            .clickable {
                scope.launch {
                    // Equivalente a whileTap scale: 0.85
                    scale.animateTo(0.85f, tween(80))

                    if (completedToday) {
                        scale.animateTo(1f, tween(120))
                    } else {
                        // Equivalente a animate isTapped: [1, 1.3, 1]
                        scale.animateTo(1.3f, tween(120))
                        scale.animateTo(1f,   spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                    }
                    onToggle()
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        if (completedToday) {
            // Equivalente al motion.span con initial scale:0 → animate scale:1
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { visible = true }

            val checkScale by animateFloatAsState(
                targetValue = if (visible) 1f else 0f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "checkScale",
            )
            Text(
                text = "✓",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.scale(checkScale),
            )
        }
    }
}