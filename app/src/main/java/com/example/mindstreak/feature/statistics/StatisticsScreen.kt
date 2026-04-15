package com.example.mindstreak.feature.statistics

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mindstreak.core.components.ProgressRing
import com.example.mindstreak.feature.home.AppViewModel
import kotlin.math.roundToInt
import androidx.compose.foundation.Canvas

// Colores del tema oscuro específico de Stats
private val StatsBg = Color(0xFF0A0A14)
private val CardBg = Color(0xFF13131F)
private val BorderColor = Color(0xFF2A2A45)
private val AccentPurple = Color(0xFF7C6EFF)
private val AccentTeal = Color(0xFF4ECDC4)

@Composable
fun StatisticsScreen(
    appViewModel: AppViewModel,
    navController: NavController
) {
    val uiState by appViewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf("Week") }
    val tabs = listOf("Week", "Month", "Year")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(StatsBg),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // ── Header ───────────────────────────────────────────
        item {
            Column(Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
                Text("Statistics", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Text("Wednesday, April 8", fontSize = 13.sp, color = Color(0xFF6B6B8A))
            }
        }

        // ── Top Stats Cards ──────────────────────────────────
        item {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatSmallCard("📅", "33/42", "This Week", AccentPurple, Modifier.weight(1f))
                StatSmallCard("🔥", "34", "Best Streak", Color(0xFFFF6B35), Modifier.weight(1f))
                StatSmallCard("⚡", "4.7", "Avg/Day", AccentTeal, Modifier.weight(1f))
            }
        }

        // ── Tab Picker ───────────────────────────────────────
        item {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .background(Color(0xFF1C1C2E), RoundedCornerShape(16.dp))
                    .padding(4.dp)
            ) {
                tabs.forEach { t ->
                    val isSelected = selectedTab == t
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) AccentPurple else Color.Transparent)
                            .clickableWithoutRipple { selectedTab = t }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = t,
                            color = if (isSelected) Color.White else Color(0xFF5A5A7A),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // ── Bar Chart (Habits Completed) ─────────────────────
        item {
            HabitsBarChart(selectedTab)
        }

        // ── Line Chart (Completion Trend) ────────────────────
        item {
            CompletionTrendChart()
        }

        // ── Habit Breakdown ──────────────────────────────────
        item {
            Text(
                "Habit Breakdown",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        itemsIndexed(uiState.habits) { index, habit ->
            HabitBreakdownRow(habit, index)
        }

        // ── Overall Summary Ring ─────────────────────────────
        item {
            SummaryRingCard(uiState.completionPercent, uiState.completedToday, uiState.totalHabits)
        }
    }
}

@Composable
private fun StatSmallCard(emoji: String, value: String, label: String, color: Color, modifier: Modifier) {
    Column(
        modifier = modifier
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
            .background(CardBg, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Text(emoji, fontSize = 18.sp)
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = color, modifier = Modifier.padding(top = 4.dp))
        Text(label.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A4A6A), letterSpacing = 0.5.sp)
    }
}

@Composable
private fun HabitsBarChart(tab: String) {
    val chartData = remember(tab) {
        when (tab) {
            "Week" -> listOf("Mon" to 0.6f, "Tue" to 1.0f, "Wed" to 0.7f, "Thu" to 1.0f, "Fri" to 0.8f, "Sat" to 0.5f, "Sun" to 0.6f)
            "Month" -> listOf("Wk 1" to 0.5f, "Wk 2" to 0.7f, "Wk 3" to 0.6f, "Wk 4" to 0.8f)
            "Year" -> listOf("Jan" to 0.6f, "Feb" to 0.8f, "Mar" to 0.9f, "Apr" to 0.7f, "May" to 0f, "Jun" to 0f, "Jul" to 0f, "Aug" to 0f, "Sep" to 0f, "Oct" to 0f, "Nov" to 0f, "Dec" to 0f)
            else -> emptyList()
        }
    }

    // Estado para el tooltip
    var selectedIndex by remember { mutableStateOf(-1) }

    Card(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp).fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Habits Completed", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(24.dp))

            Box(contentAlignment = Alignment.TopCenter) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(140.dp),
                    horizontalArrangement = Arrangement.spacedBy(if(tab == "Year") 6.dp else 12.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    chartData.forEachIndexed { index, (label, progress) ->
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                // HACEMOS CLICKEABLE TODA LA COLUMNA
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
                                Box(modifier = Modifier.fillMaxSize().background(Color(0xFF232335)))

                                val animatedHeight by animateFloatAsState(
                                    targetValue = progress,
                                    animationSpec = tween(800),
                                    label = ""
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(animatedHeight)
                                        // Si está seleccionado, cambia de color
                                        .background(if (selectedIndex == index) Color.White else AccentPurple)
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(text = label, color = Color(0xFF5A5A7A), fontSize = 10.sp)
                        }
                    }
                }

                // TOOLTIP FLOTANTE (Igual que en React)
                if (selectedIndex != -1) {
                    val data = chartData[selectedIndex]
                    Surface(
                        color = Color(0xFF1C1C2E),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                        modifier = Modifier.offset(y = (-30).dp) // Un poco más arriba de las barras
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            Text(data.first, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("done : ${(data.second * 6).toInt()}", color = AccentPurple, fontSize = 11.sp)
                            Text("total : 6", color = Color(0xFF6B6B8A), fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CompletionTrendChart() {
    // Datos: Día vs Tasa de completado (0f a 100f)
    val trendData = listOf(
        60f, 65f, 58f, 72f, 75f, 80f, 78f, 85f, 87f
    )

    var selectedIndex by remember { mutableStateOf(-1) }

    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
    ) {
        Column(Modifier.padding(16.dp)) {
            // Header
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Completion Trend", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.TrendingUp, null, tint = AccentTeal, modifier = Modifier.size(14.dp))
                    Text("+12%", color = AccentTeal, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Gráfico de Línea Interactivo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            // Calculamos qué punto está más cerca del toque
                            val width = size.width.toFloat()
                            val step = width / (trendData.size - 1)
                            val index = (offset.x / step).roundToInt().coerceIn(0, trendData.size - 1)
                            selectedIndex = index
                        }
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val stepX = width / (trendData.size - 1)

                    // Función para normalizar Y (invertido porque 0 es arriba)
                    val maxY = 100f
                    val minY = 0f
                    fun getCoordinateY(value: Float) = height - ((value - minY) / (maxY - minY) * height)

                    // Dibujar la línea suavizada (Cubic Curve)
                    val path = androidx.compose.ui.graphics.Path().apply {
                        trendData.forEachIndexed { i, value ->
                            val x = i * stepX
                            val y = getCoordinateY(value)
                            if (i == 0) moveTo(x, y) else {
                                // Curva suavizada aproximada
                                val prevX = (i - 1) * stepX
                                val prevY = getCoordinateY(trendData[i - 1])
                                cubicTo(
                                    (prevX + x) / 2f, prevY,
                                    (prevX + x) / 2f, y,
                                    x, y
                                )
                            }
                        }
                    }

                    drawPath(
                        path = path,
                        color = AccentTeal,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = 3.dp.toPx(),
                            cap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                    )

                    // Dibujar Indicador Vertical y Punto si hay selección
                    if (selectedIndex != -1) {
                        val selX = selectedIndex * stepX
                        val selY = getCoordinateY(trendData[selectedIndex])

                        // Línea vertical blanca sutil
                        drawLine(
                            color = Color.White.copy(alpha = 0.3f),
                            start = androidx.compose.ui.geometry.Offset(selX, 0f),
                            end = androidx.compose.ui.geometry.Offset(selX, height),
                            strokeWidth = 1.dp.toPx()
                        )

                        // Punto en la línea
                        drawCircle(
                            color = Color.White,
                            radius = 6.dp.toPx(),
                            center = androidx.compose.ui.geometry.Offset(selX, selY)
                        )
                        drawCircle(
                            color = AccentTeal,
                            radius = 4.dp.toPx(),
                            center = androidx.compose.ui.geometry.Offset(selX, selY)
                        )
                    }
                }

                // TOOLTIP (Igual que tu captura de React)
                if (selectedIndex != -1) {
                    val value = trendData[selectedIndex]
                    val stepX = 100f / (trendData.size - 1) // Aproximación para el offset

                    Surface(
                        color = Color(0xFF1C1C2E),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(x = if (selectedIndex > trendData.size/2) (-50).dp else 50.dp, y = (-20).dp)
                    ) {
                        Column(Modifier.padding(10.dp)) {
                            Text("${selectedIndex + 1}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("rate : ${value.toInt()}", color = AccentTeal, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HabitBreakdownRow(habit: com.example.mindstreak.data.model.Habit, index: Int) {
    val habitColor = Color(android.graphics.Color.parseColor(habit.color))

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .fillMaxWidth()
            .background(CardBg, RoundedCornerShape(16.dp))
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(habit.emoji, fontSize = 18.sp)
        Text(habit.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))

        Box(modifier = Modifier.width(90.dp).height(6.dp).background(BorderColor, CircleShape)) {
            val progressWidth by animateFloatAsState(targetValue = habit.completionRate, label = "breakdown", animationSpec = tween(1000, delayMillis = 300 + (index * 50)))
            Box(modifier = Modifier.fillMaxWidth(progressWidth).fillMaxHeight().background(habitColor, CircleShape))
        }

        Text("${(habit.completionRate * 100).roundToInt()}%", color = habitColor, fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.End, modifier = Modifier.width(32.dp))
    }
}

@Composable
private fun SummaryRingCard(percent: Int, done: Int, total: Int) {
    Card(
        modifier = Modifier.padding(20.dp).fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ProgressRing(progress = percent.toFloat(), size = 70.dp, color = AccentPurple, trackColor = BorderColor) {
                Text("$percent%", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
            }
            Column {
                Text("Overall Today", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text("$done of $total habits done", color = Color(0xFF6B6B8A), fontSize = 12.sp)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(top = 8.dp)) {
                    Icon(Icons.Default.Adjust, null, tint = AccentPurple, modifier = Modifier.size(14.dp))
                    Text("${total - done} more to go!", color = AccentPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Extension para evitar el efecto de ripple visual al clickear pestañas
@Composable
fun Modifier.clickableWithoutRipple(onClick: () -> Unit): Modifier = this.clickable(
    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
    indication = null,
    onClick = onClick
)

