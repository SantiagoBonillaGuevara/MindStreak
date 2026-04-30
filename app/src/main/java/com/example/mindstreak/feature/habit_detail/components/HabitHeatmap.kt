package com.example.mindstreak.feature.habit_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
fun HabitHeatmap(
    monthTitle: String,
    levelColors: List<Color>,
    gridData: List<Any?>, // Usamos Any? para simplificar el desacoplamiento de la data mock
    lessLabel: String,
    moreLabel: String,
    dayLabels: List<String>,
    modifier: Modifier = Modifier,
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(monthTitle, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        lessLabel,
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    levelColors.forEach { color ->
                        Box(Modifier
                            .size(10.dp)
                            .background(color, RoundedCornerShape(2.dp)))
                    }
                    Text(
                        moreLabel,
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    dayLabels.forEach {
                        Text(
                            it,
                            modifier = Modifier.weight(1f),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                // Aquí delegamos el renderizado de los chunks por simplicidad en este componente
                // En una app real, gridData sería una estructura más formal
                val chunks = gridData.chunked(7)
                chunks.forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        row.forEach { cell ->
                            // Lógica de renderizado simplificada basada en la estructura original
                            val level = try {
                                val field = cell?.javaClass?.getDeclaredField("level")
                                field?.isAccessible = true
                                field?.get(cell) as Int
                            } catch (_: Exception) {
                                0
                            }

                            val day = try {
                                val field = cell?.javaClass?.getDeclaredField("day")
                                field?.isAccessible = true
                                field?.get(cell) as Int
                            } catch (_: Exception) {
                                -1
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(22.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (cell != null) levelColors.getOrElse(level) { Color.Transparent } else Color.Transparent),
                                contentAlignment = Alignment.Center
                            ) {
                                if (day == 8) {
                                    Box(
                                        Modifier
                                            .size(4.dp)
                                            .background(Color.White.copy(alpha = 0.6f), CircleShape)
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
