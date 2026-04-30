package com.example.mindstreak.feature.streak.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt

@Composable
fun HabitStreakRow(
    emoji: String,
    name: String,
    streak: Int,
    colorHex: String,
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
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(emoji, fontSize = 20.sp)
        Text(name, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        LinearProgressIndicator(
            progress = { (streak / 30f).coerceIn(0f, 1f) },
            modifier = Modifier
                .width(80.dp)
                .height(6.dp)
                .clip(CircleShape),
            color = habitColor, trackColor = MaterialTheme.colorScheme.secondary
        )
        Text("🔥$streak", fontSize = 12.sp, fontWeight = FontWeight.Black, color = habitColor)
    }
}
