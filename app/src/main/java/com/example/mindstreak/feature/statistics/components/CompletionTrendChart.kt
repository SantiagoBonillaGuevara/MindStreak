package com.example.mindstreak.feature.statistics.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.theme.HabitTeal
import kotlin.math.roundToInt

@Composable
fun CompletionTrendChart(
    title: String,
    trendText: String,
    rateLabel: String,
    data: List<Float>,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

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
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.TrendingUp,
                        null,
                        tint = HabitTeal,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        trendText,
                        color = HabitTeal,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val step = size.width / (data.size - 1)
                        selectedIndex = (offset.x / step).roundToInt().coerceIn(0, data.size - 1)
                    }
                }) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val stepX = width / (data.size - 1)
                    fun getCoordinateY(v: Float) = height - (v / 100f * height)
                    val path = Path().apply {
                        data.forEachIndexed { i, value ->
                            val x = i * stepX
                            val y = getCoordinateY(value)
                            if (i == 0) moveTo(x, y) else {
                                val prevX = (i - 1) * stepX
                                val prevY = getCoordinateY(data[i - 1])
                                cubicTo((prevX + x) / 2f, prevY, (prevX + x) / 2f, y, x, y)
                            }
                        }
                    }
                    drawPath(
                        path = path,
                        color = HabitTeal,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )
                    if (selectedIndex != -1) {
                        val selX = selectedIndex * stepX
                        val selY = getCoordinateY(data[selectedIndex])
                        drawLine(
                            color = onSurfaceColor.copy(alpha = 0.3f),
                            start = Offset(selX, 0f),
                            end = Offset(selX, height),
                            strokeWidth = 1.dp.toPx()
                        )
                        drawCircle(
                            color = onSurfaceColor,
                            radius = 6.dp.toPx(),
                            center = Offset(selX, selY)
                        )
                        drawCircle(
                            color = HabitTeal,
                            radius = 4.dp.toPx(),
                            center = Offset(selX, selY)
                        )
                    }
                }
                if (selectedIndex != -1) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(
                                x = if (selectedIndex > data.size / 2) (-50).dp else 50.dp,
                                y = (-20).dp
                            )
                    ) {
                        Column(Modifier.padding(10.dp)) {
                            Text(
                                "${selectedIndex + 1}",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                "$rateLabel : ${data[selectedIndex].toInt()}",
                                color = HabitTeal,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
