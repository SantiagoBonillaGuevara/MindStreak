package com.example.mindstreak.feature.statistics.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import androidx.core.graphics.toColorInt

@Composable
fun HabitBreakdownRow(
    emoji: String,
    name: String,
    completionRate: Float,
    colorHex: String,
    index: Int,
    modifier: Modifier = Modifier
) {
    val habitColor = try {
        Color(colorHex.toColorInt())
    } catch (_: Exception) {
        MaterialTheme.colorScheme.primary
    }

    Row(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(emoji, fontSize = 18.sp)
        Text(
            name,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier
                .width(90.dp)
                .height(6.dp)
                .background(MaterialTheme.colorScheme.outlineVariant, CircleShape)
        ) {
            val progressWidth by animateFloatAsState(
                targetValue = completionRate,
                label = "breakdown",
                animationSpec = tween(1000, delayMillis = 300 + (index * 50))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(progressWidth)
                    .fillMaxHeight()
                    .background(habitColor, CircleShape)
            )
        }
        Text(
            "${(completionRate * 100).roundToInt()}%",
            color = habitColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.width(32.dp)
        )
    }
}
