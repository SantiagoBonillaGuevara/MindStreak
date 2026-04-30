package com.example.mindstreak.feature.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.theme.*

@Composable
fun ProfileStatsGrid(
    stats: List<Pair<String, String>>,
    modifier: Modifier = Modifier
) {
    val colors = listOf(HabitOrange, HabitYellow, HabitPurple, HabitTeal)

    Row(
        modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        stats.forEachIndexed { i, stat ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stat.second,
                    color = colors.getOrElse(i) { HabitPurple },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    stat.first.uppercase(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
