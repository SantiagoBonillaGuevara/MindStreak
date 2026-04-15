package com.example.mindstreak.feature.habit_detail

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.components.ProgressRing
import com.example.mindstreak.feature.home.AppViewModel
import kotlin.math.roundToInt

// Mantenemos el Grid visual del mes como un mock por ahora
private data class DayCell(val day: Int, val level: Int)
private val MONTH_GRID: List<DayCell?> = List(35) { i ->
    if (i < 3) null
    else {
        val day = i - 2
        if (day > 30) null
        else DayCell(day, (0..3).random())
    }
}

@Composable
fun HabitDetailScreen(
    habitId: String,
    appViewModel: AppViewModel,
    onBack: () -> Unit,
) {
    // 1. Obtenemos el estado real del ViewModel
    val uiState by appViewModel.uiState.collectAsState()

    // 2. Buscamos el hábito específico por su ID
    val habit = remember(uiState.habits, habitId) {
        uiState.habits.find { it.id == habitId }
    }

    // 3. Fallback si el hábito no existe (fue borrado o ID incorrecto)
    if (habit == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Habit not found", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onBack) { Text("Return Home") }
            }
        }
        return
    }

    // 4. Variables derivadas del hábito real
    val habitColor = remember(habit.color) {
        try {
            Color(android.graphics.Color.parseColor(habit.color))
        } catch (e: Exception) {
            Color(0xFF8B5CF6) // Violeta por defecto si el hex falla
        }
    }

    val levelColors = listOf(
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
        habitColor.copy(alpha = 0.25f),
        habitColor.copy(alpha = 0.5f),
        habitColor
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Header ───────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilledTonalIconButton(
                onClick = onBack,
                modifier = Modifier.size(36.dp),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = habit.name,
                modifier = Modifier.weight(1f),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )
            FilledTonalIconButton(
                onClick = { /* Menú de opciones adicionales */ },
                modifier = Modifier.size(36.dp),
                shape = CircleShape
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "More", modifier = Modifier.size(16.dp))
            }
        }

        // ── Hero Card ────────────────────────────────────────
        Card(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.linearGradient(
                            colors = listOf(habitColor.copy(alpha = 0.15f), habitColor.copy(alpha = 0.05f))
                        )
                    )
                    .border(1.dp, habitColor.copy(alpha = 0.2f), RoundedCornerShape(28.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(habitColor.copy(alpha = 0.12f), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(habit.emoji, fontSize = 32.sp)
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(
                                habit.category.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                letterSpacing = 1.sp
                            )
                            Text(habit.name, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                            Text(
                                "⏰ ${habit.frequency} at ${habit.reminderTime}",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.padding(top = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatItem(Modifier.weight(1f), habit.streak.toString(), "Current", habitColor)
                        StatItem(Modifier.weight(1f), "34", "Best", Color(0xFFFB923C))
                        StatItem(Modifier.weight(1f), "${(habit.completionRate * 100).roundToInt()}%", "Rate", Color(0xFF2DD4BF))
                    }
                }
            }
        }

        // ── This Week ───────────────────────────────────────
        Text(
            "This Week",
            modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 12.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("M", "T", "W", "T", "F", "S", "S").forEachIndexed { i, day ->
                val isDone = habit.weekHistory.getOrElse(i) { false }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(day, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isDone) habitColor.copy(alpha = 0.15f) else MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
                            .border(
                                width = if (isDone) 1.5.dp else 1.dp,
                                color = if (isDone) habitColor.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isDone) Text("✓", color = habitColor, fontWeight = FontWeight.Bold)
                        else Text("-", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                    }
                }
            }
        }

        // ── Heatmap ──────────────────────────────────────────
        Card(
            modifier = Modifier.padding(20.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("April 2026", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Less", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        levelColors.forEach { color ->
                            Box(Modifier.size(10.dp).background(color, RoundedCornerShape(2.dp)))
                        }
                        Text("More", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    }
                }

                Spacer(Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        listOf("M", "T", "W", "T", "F", "S", "S").forEach {
                            Text(it, modifier = Modifier.weight(1f), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f), textAlign = TextAlign.Center)
                        }
                    }
                    val chunks = MONTH_GRID.chunked(7)
                    chunks.forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            row.forEach { cell ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(22.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (cell != null) levelColors[cell.level] else Color.Transparent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (cell?.day == 8) {
                                        Box(Modifier.size(4.dp).background(Color.White.copy(alpha = 0.6f), CircleShape))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ── Completion Rate Card ─────────────────────────────
        Card(
            modifier = Modifier.padding(horizontal = 20.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProgressRing(
                    progress = habit.completionRate * 100,
                    size = 64.dp,
                    strokeWidth = 6.dp,
                    color = habitColor
                ) {
                    Text("${(habit.completionRate * 100).roundToInt()}%", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                }
                Column {
                    Text("Completion Rate", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text("Last 30 days", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.TrendingUp, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color(0xFF2DD4BF))
                        Text("+8% vs last month", color = Color(0xFF2DD4BF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // ── Action Buttons ───────────────────────────────────
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { /* Navegar a edición */ },
                modifier = Modifier.weight(1f).height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("Edit", fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = {
                    appViewModel.deleteHabit(habit.id)
                    onBack()
                },
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f)),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete habit")
            }
        }
    }
}

@Composable
fun StatItem(modifier: Modifier, value: String, label: String, color: Color) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black.copy(alpha = 0.2f))
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Black, color = color)
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
    }
}