package com.example.mindstreak.feature.streak.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalendarCard(
    monthTitle: String,
    streakDayLabel: String,
    dayLabels: List<String>,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(20.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary.copy(
                alpha = 0.1f
            )
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(monthTitle, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(Modifier
                        .size(10.dp)
                        .background(color, RoundedCornerShape(2.dp)))
                    Text(
                        streakDayLabel,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row {
                    dayLabels.forEach {
                        Text(
                            it,
                            Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
                (1..5).forEach { week ->
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        (1..7).forEach { day ->
                            val dayNum = (week - 1) * 7 + day - 4
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(28.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (dayNum in 1..8) color.copy(alpha = 0.3f) else MaterialTheme.colorScheme.secondary.copy(
                                            alpha = 0.2f
                                        )
                                    )
                                    .border(
                                        if (dayNum == 8) 2.dp else 0.dp,
                                        color,
                                        RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (dayNum in 1..30) {
                                    Text(
                                        "$dayNum",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (dayNum == 8) color else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
