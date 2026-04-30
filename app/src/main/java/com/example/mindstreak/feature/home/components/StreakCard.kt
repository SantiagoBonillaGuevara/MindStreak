package com.example.mindstreak.feature.home.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.theme.HabitOrange
import com.example.mindstreak.core.theme.HabitYellow
import kotlinx.coroutines.delay

@Composable
fun StreakCard(
    currentStreak: Int,
    label: String,
    daysSuffix: String,
    nextMilestoneText: String,
    bestStreakText: String,
    goalText: String,
    dayZeroLabel: String,
    dayCurrentLabel: String,
    dayGoalLabel: String,
    weekDays: List<String>,
    todayIndex: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(
                alpha = 0.6f
            )
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(144.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-32).dp)
                    .clip(CircleShape)
                    .background(HabitOrange.copy(alpha = 0.15f))
                    .blur(24.dp),
            )

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.8.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        )
                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            var visible by remember { mutableStateOf(false) }
                            LaunchedEffect(Unit) {
                                delay(300)
                                visible = true
                            }
                            val scale by animateFloatAsState(
                                targetValue = if (visible) 1f else 0.5f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = 250f
                                ),
                                label = "streakScale",
                            )
                            Text(
                                text = "$currentStreak",
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 48.sp),
                                color = HabitOrange,
                                modifier = Modifier.graphicsLayer {
                                    scaleX = scale; scaleY = scale
                                },
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = daysSuffix,
                                style = MaterialTheme.typography.titleMedium,
                                color = HabitOrange,
                                modifier = Modifier.padding(bottom = 6.dp),
                            )
                        }
                        Text(
                            text = nextMilestoneText,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(HabitOrange.copy(alpha = 0.1f))
                                .border(1.dp, HabitOrange.copy(alpha = 0.2f), CircleShape)
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                        ) {
                            Text(
                                text = bestStreakText,
                                style = MaterialTheme.typography.labelMedium,
                                color = HabitOrange
                            )
                        }
                        Text(
                            text = goalText,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                val progressWidth by animateFloatAsState(
                    targetValue = (currentStreak / 30f).coerceIn(0f, 1f),
                    animationSpec = tween(1000, delayMillis = 400, easing = FastOutSlowInEasing),
                    label = "streakBar",
                )
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progressWidth)
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(
                                            HabitOrange,
                                            HabitYellow
                                        )
                                    )
                                )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            dayZeroLabel,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                        Text(
                            dayCurrentLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = HabitOrange
                        )
                        Text(
                            dayGoalLabel,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    weekDays.forEachIndexed { i, d ->
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = d,
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(CircleShape)
                                    .background(if (i <= todayIndex) HabitOrange else MaterialTheme.colorScheme.secondary),
                            )
                        }
                    }
                }
            }
        }
    }
}
