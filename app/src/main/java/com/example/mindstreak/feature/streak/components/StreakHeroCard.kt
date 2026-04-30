package com.example.mindstreak.feature.streak.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.components.StatMini

@Composable
fun StreakHeroCard(
    streak: Int,
    color: Color,
    streakLabel: String,
    sinceText: String,
    bestEverLabel: String,
    bestEverValue: String,
    totalLogsLabel: String,
    totalLogsValue: String,
    thisMonthLabel: String,
    thisMonthValue: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "emojiScale"
    )

    Card(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = 0.3f
            )
        )
    ) {
        Box(modifier = Modifier
            .padding(vertical = 24.dp)
            .fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopCenter)
                    .graphicsLayer { translationY = -100f }
                    .background(
                        Brush.radialGradient(
                            listOf(
                                color.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    ))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "🔥",
                    fontSize = 60.sp,
                    modifier = Modifier.graphicsLayer { scaleX = scale; scaleY = scale })
                Text(
                    "$streak",
                    fontSize = 88.sp,
                    fontWeight = FontWeight.Black,
                    color = color,
                    lineHeight = 88.sp
                )
                Text(
                    streakLabel,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = color.copy(alpha = 0.8f)
                )
                Text(
                    sinceText,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Row(
                    modifier = Modifier.padding(top = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    StatMini(bestEverValue, bestEverLabel)
                    Box(
                        Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    )
                    StatMini(totalLogsValue, totalLogsLabel)
                    Box(
                        Modifier
                            .width(1.dp)
                            .height(30.dp)
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    )
                    StatMini(thisMonthValue, thisMonthLabel)
                }
            }
        }
    }
}
