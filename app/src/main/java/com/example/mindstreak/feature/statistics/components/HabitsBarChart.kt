package com.example.mindstreak.feature.statistics.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.components.clickableWithoutRipple
import com.example.mindstreak.core.theme.HabitPurple

@Composable
fun HabitsBarChart(
    title: String,
    data: List<Pair<String, Float>>,
    doneLabel: String,
    totalLabel: String,
    isYearView: Boolean,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }

    Card(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(24.dp))
            Box(contentAlignment = Alignment.TopCenter) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    horizontalArrangement = Arrangement.spacedBy(if (isYearView) 6.dp else 12.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    data.forEachIndexed { index, (label, progress) ->
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickableWithoutRipple {
                                    selectedIndex = if (selectedIndex == index) -1 else index
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                        )
                                )
                                val animatedHeight by animateFloatAsState(
                                    targetValue = progress,
                                    animationSpec = tween(800),
                                    label = ""
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(animatedHeight)
                                        .background(if (selectedIndex == index) MaterialTheme.colorScheme.primary else HabitPurple)
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = label,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
                if (selectedIndex != -1) {
                    val selectedData = data[selectedIndex]
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier.offset(y = (-30).dp)
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            Text(
                                selectedData.first,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                "$doneLabel : ${(selectedData.second * 6).toInt()}",
                                color = HabitPurple,
                                fontSize = 11.sp
                            )
                            Text(
                                "$totalLabel : 6",
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
