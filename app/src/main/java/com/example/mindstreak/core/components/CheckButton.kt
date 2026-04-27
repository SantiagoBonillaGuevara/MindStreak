package com.example.mindstreak.core.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.theme.HabitDarkGreen
import com.example.mindstreak.core.theme.HabitTeal
import kotlinx.coroutines.launch

@Composable
fun CheckButton(
    completedToday: Boolean,
    onToggle: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    // Equivalente a whileTap={{ scale: 0.85 }} y animate isTapped
    val scale = remember { Animatable(1f) }
    // Colores del botón según estado
    val completedGradient = Brush.linearGradient(
        colors = listOf(HabitTeal, HabitDarkGreen) // teal-400 → teal-500
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
                        scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
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