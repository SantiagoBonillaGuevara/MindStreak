package com.example.mindstreak.feature.streak

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mindstreak.feature.home.AppViewModel
import com.example.mindstreak.feature.streak.components.*
import com.example.mindstreak.core.theme.HabitOrange

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

@SuppressLint("DefaultLocale")
@Composable
fun StreakScreen(appViewModel: AppViewModel, navController: NavController) {
    val uiState by appViewModel.uiState.collectAsState()
    val orangeMain = HabitOrange

    val texts = object {
        val title = "Streak"
        val shieldLabel = "Streak Shield"
        val streakLabel = "Day Streak"
        val sinceText = "Since March 18, 2026"
        val bestEverLabel = "Best Ever"
        val totalLogsLabel = "Total Logs"
        val thisMonthLabel = "This Month"
        val nextMilestoneTitle = "Next Milestone"
        val awayTemplate = "%d days away 🌟"
        val dayLabelTemplate = "Day %d"
        val milestonesTitle = "Milestones"
        val daysLabel = "days"
        val remainingTemplate = "%dd"
        val calendarMonth = "April 2026"
        val streakDayLabel = "Streak day"
        val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")
        val ctaTitle = "Don't break the chain!"
        val ctaSubTemplate = "Log today's habits to keep your %d-day streak alive."
        val habitStreaksTitle = "Habit Streaks"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item { StreakHeader(texts.title, texts.shieldLabel, orangeMain) }
        item {
            StreakHeroCard(
                uiState.currentStreak,
                orangeMain,
                texts.streakLabel,
                texts.sinceText,
                texts.bestEverLabel,
                "34",
                texts.totalLogsLabel,
                "342",
                texts.thisMonthLabel,
                "87%"
            )
        }
        item {
            NextMilestoneCard(
                uiState.currentStreak,
                orangeMain,
                texts.nextMilestoneTitle,
                texts.awayTemplate,
                texts.dayLabelTemplate,
                30
            )
        }
        item {
            Text(
                texts.milestonesTitle,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
        itemsIndexed(MILESTONES) { _, m ->
            MilestoneItem(
                m.emoji,
                m.label,
                m.days,
                m.achieved,
                m.current,
                uiState.currentStreak,
                orangeMain,
                texts.daysLabel,
                texts.remainingTemplate
            )
        }
        item {
            CalendarCard(
                texts.calendarMonth,
                texts.streakDayLabel,
                texts.dayLabels,
                orangeMain
            )
        }
        item {
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .clickable { navController.navigate("home") },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = orangeMain.copy(alpha = 0.1f)),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    orangeMain.copy(alpha = 0.2f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Default.Bolt,
                        null,
                        tint = orangeMain,
                        modifier = Modifier.size(24.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(texts.ctaTitle, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                        Text(
                            String.format(texts.ctaSubTemplate, uiState.currentStreak),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    Icon(Icons.Default.ChevronRight, null, tint = orangeMain)
                }
            }
        }
        item {
            Text(
                texts.habitStreaksTitle,
                modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
        itemsIndexed(uiState.habits) { _, habit ->
            HabitStreakRow(
                habit.emoji,
                habit.name,
                habit.streak,
                habit.color
            )
        }
    }
}
