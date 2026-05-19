package com.example.mindstreak.feature.habit_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.components.ProgressRing
import com.example.mindstreak.core.theme.Destructive
import com.example.mindstreak.core.theme.HabitTeal
import kotlin.math.roundToInt

@Composable
fun HabitCompletionRateCard(
    completionRate: Float,
    habitColor: Color,
    title: String,
    subtitle: String,
    comparisonText: String,
    modifier: Modifier = Modifier,
) {
    val isNegative = comparisonText.startsWith("-")
    val trendColor = if (isNegative) Destructive else HabitTeal
    val trendIcon = if (isNegative) Icons.AutoMirrored.Filled.TrendingDown else Icons.AutoMirrored.Filled.TrendingUp

    Card(
        modifier = modifier.padding(horizontal = 20.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary.copy(
                alpha = 0.1f
            )
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProgressRing(
                progress = completionRate * 100,
                size = 64.dp,
                strokeWidth = 6.dp,
                color = habitColor
            ) {
                Text(
                    "${(completionRate * 100).roundToInt()}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Column {
                Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(
                    subtitle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        trendIcon,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = trendColor
                    )
                    Text(
                        comparisonText,
                        color = trendColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
