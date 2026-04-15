package com.example.mindstreak.feature.streak

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mindstreak.feature.home.AppViewModel

// ── Modelos para la pantalla ─────────────────────────────────
data class Milestone(
    val days: Int,
    val emoji: String,
    val label: String,
    val achieved: Boolean,
    val current: Boolean = false
)

private val MILESTONES = listOf(
    Milestone(7, "🔥", "First Flame", true),
    Milestone(14, "💪", "Iron Will", true),
    Milestone(21, "⚡", "Current", true, current = true),
    Milestone(30, "🌟", "Habit Hero", false),
    Milestone(50, "💎", "Diamond Mind", false),
    Milestone(100, "👑", "Legend", false)
)

@Composable
fun StreakScreen(
    appViewModel: AppViewModel,
    navController: NavController
) {
    val uiState by appViewModel.uiState.collectAsState()
    val orangeMain = Color(0xFFF97316)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // ── Header ───────────────────────────────────────────
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Streak",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.weight(1f)
                )
                Row(
                    modifier = Modifier
                        .background(orangeMain.copy(alpha = 0.1f), CircleShape)
                        .border(1.dp, orangeMain.copy(alpha = 0.3f), CircleShape)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(12.dp), tint = orangeMain)
                    Text("Streak Shield", color = orangeMain, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // ── Hero Streak Card ─────────────────────────────────
        item {
            StreakHeroCard(uiState.currentStreak, orangeMain)
        }

        // ── Next Milestone ───────────────────────────────────
        item {
            NextMilestoneCard(uiState.currentStreak, orangeMain)
        }

        // ── Milestones List ──────────────────────────────────
        item {
            Text(
                "Milestones",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        itemsIndexed(MILESTONES) { index, m ->
            MilestoneItem(m, uiState.currentStreak, orangeMain)
        }

        // ── Calendar (Abril) ─────────────────────────────────
        item {
            CalendarCard(orangeMain)
        }

        // ── CTA: Don't break the chain ───────────────────────
        item {
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .clickable { navController.navigate("home") },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = orangeMain.copy(alpha = 0.1f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, orangeMain.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Default.Bolt, contentDescription = null, tint = orangeMain, modifier = Modifier.size(24.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Don't break the chain!", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Log today's habits to keep your ${uiState.currentStreak}-day streak alive.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = orangeMain)
                }
            }
        }

        // ── Per-habit Streaks ────────────────────────────────
        item {
            Text(
                "Habit Streaks",
                modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        itemsIndexed(uiState.habits) { _, habit ->
            HabitStreakRow(habit)
        }
    }
}

@Composable
private fun StreakHeroCard(streak: Int, color: Color) {
    // Animación de pulso para el emoji
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "emojiScale"
    )

    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Box(modifier = Modifier.padding(vertical = 24.dp).fillMaxWidth()) {
            // Glow effect
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopCenter)
                    .graphicsLayer { translationY = -100f }
                    .background(Brush.radialGradient(listOf(color.copy(alpha = 0.15f), Color.Transparent)))
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("🔥", fontSize = 60.sp, modifier = Modifier.graphicsLayer { scaleX = scale; scaleY = scale })
                Text("$streak", fontSize = 88.sp, fontWeight = FontWeight.Black, color = color, lineHeight = 88.sp)
                Text("Day Streak", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = color.copy(alpha = 0.8f))
                Text("Since March 18, 2026", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))

                Row(modifier = Modifier.padding(top = 24.dp), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    StatMini("34", "Best Ever")
                    Box(Modifier.width(1.dp).height(30.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)))
                    StatMini("342", "Total Logs")
                    Box(Modifier.width(1.dp).height(30.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)))
                    StatMini("87%", "This Month")
                }
            }
        }
    }
}

@Composable
private fun NextMilestoneCard(streak: Int, color: Color) {
    Card(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Next Milestone", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text("${30 - streak} days away 🌟", fontSize = 13.sp, color = color, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("🌟", fontSize = 28.sp)
                Column(modifier = Modifier.weight(1f)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Day $streak", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        Text("Day 30", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    }
                    Spacer(Modifier.height(6.dp))
                    val progress = (streak / 30f).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                        color = color,
                        trackColor = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

@Composable
private fun MilestoneItem(m: Milestone, currentStreak: Int, color: Color) {
    val bgColor = if (m.current) color.copy(alpha = 0.1f) else MaterialTheme.colorScheme.secondary.copy(alpha = if (m.achieved) 0.3f else 0.1f)
    val borderColor = if (m.current) color.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(m.emoji, fontSize = 24.sp, modifier = Modifier.alpha(if (m.achieved) 1f else 0.5f))
        Column(modifier = Modifier.weight(1f)) {
            Text(m.label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (m.achieved) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            Text("${m.days} days", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        }
        if (m.achieved) {
            Box(
                modifier = Modifier.size(24.dp).background(if (m.current) color else Color(0xFF2DD4BF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("✓", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black)
            }
        } else {
            Text("${m.days - currentStreak}d", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        }
    }
}

@Composable
private fun CalendarCard(color: Color) {
    Card(
        modifier = Modifier.padding(20.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("April 2026", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(Modifier.size(10.dp).background(color, RoundedCornerShape(2.dp)))
                    Text("Streak day", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                }
            }
            Spacer(Modifier.height(12.dp))

            // Grid de Calendario
            val days = listOf("M", "T", "W", "T", "F", "S", "S")
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row {
                    days.forEach { Text(it, Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)) }
                }
                // Ejemplo rápido de una fila del calendario
                (1..5).forEach { week ->
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        (1..7).forEach { day ->
                            val dayNum = (week - 1) * 7 + day - 4
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(28.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (dayNum in 1..8) color.copy(alpha = 0.3f) else MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                                    .border(if (dayNum == 8) 2.dp else 0.dp, color, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (dayNum in 1..30) {
                                    Text("$dayNum", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (dayNum == 8) color else MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HabitStreakRow(habit: com.example.mindstreak.data.model.Habit) {
    val habitColor = Color(android.graphics.Color.parseColor(habit.color))
    Row(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(habit.emoji, fontSize = 20.sp)
        Text(habit.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        LinearProgressIndicator(
            progress = { (habit.streak / 30f).coerceIn(0f, 1f) },
            modifier = Modifier.width(80.dp).height(6.dp).clip(CircleShape),
            color = habitColor,
            trackColor = MaterialTheme.colorScheme.secondary
        )
        Text("🔥${habit.streak}", fontSize = 12.sp, fontWeight = FontWeight.Black, color = habitColor)
    }
}

@Composable
fun StatMini(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
    }
}