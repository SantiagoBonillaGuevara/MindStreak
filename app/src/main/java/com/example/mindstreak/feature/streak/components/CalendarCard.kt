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

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import androidx.compose.ui.platform.LocalLocale

@Composable
fun CalendarCard(
    streakDayLabel: String,
    dayLabels: List<String>,
    activeDays: Set<String>, // "YYYY-MM-DD"
    color: Color,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val currentMonth = YearMonth.from(today)
    val firstOfMonth = currentMonth.atDay(1)
    val monthTitle = firstOfMonth.month.getDisplayName(TextStyle.FULL, LocalLocale.current.platformLocale)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(LocalLocale.current.platformLocale) else it.toString() } +
        " ${today.year}"

    // If you prefer Monday as first day:
    val startOffset = firstOfMonth.dayOfWeek.value - 1 // 0=Mon, ..., 6=Sun

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
                
                val daysInMonth = currentMonth.lengthOfMonth()
                
                (0 until 6).forEach { week ->
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        (0 until 7).forEach { dayOfWeek ->
                            val slotIndex = week * 7 + dayOfWeek
                            val dayNum = slotIndex - startOffset + 1
                            
                            val isWithinMonth = dayNum in 1..daysInMonth
                            val dateStr = if (isWithinMonth) {
                                currentMonth.atDay(dayNum).toString()
                            } else null
                            
                            val isActive = dateStr != null && activeDays.contains(dateStr)
                            val isToday = dateStr != null && LocalDate.now().toString() == dateStr

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(28.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        when {
                                            isActive -> color.copy(alpha = 0.3f)
                                            isWithinMonth -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                            else -> Color.Transparent
                                        }
                                    )
                                    .border(
                                        if (isToday) 1.dp else 0.dp,
                                        if (isToday) color else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isWithinMonth) {
                                    Text(
                                        "$dayNum",
                                        fontSize = 10.sp,
                                        fontWeight = if (isActive || isToday) FontWeight.Bold else FontWeight.Normal,
                                        color = when {
                                            isActive -> color
                                            isToday -> color
                                            else -> MaterialTheme.colorScheme.onSurface
                                        }
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
