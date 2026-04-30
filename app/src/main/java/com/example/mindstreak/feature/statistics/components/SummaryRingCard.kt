package com.example.mindstreak.feature.statistics.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.components.ProgressRing
import com.example.mindstreak.core.theme.HabitPurple

@Composable
fun SummaryRingCard(
    percent: Int,
    done: Int,
    total: Int,
    title: String,
    subtitleTemplate: String, // e.g. "%d of %d habits done"
    moreToGoTemplate: String, // e.g. "%d more to go!"
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProgressRing(
                progress = percent.toFloat(),
                size = 70.dp,
                color = HabitPurple,
                trackColor = MaterialTheme.colorScheme.outlineVariant
            ) {
                Text(
                    "$percent%",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Column {
                Text(
                    title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    String.format(subtitleTemplate, done, total),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Adjust,
                        null,
                        tint = HabitPurple,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        String.format(moreToGoTemplate, total - done),
                        color = HabitPurple,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
