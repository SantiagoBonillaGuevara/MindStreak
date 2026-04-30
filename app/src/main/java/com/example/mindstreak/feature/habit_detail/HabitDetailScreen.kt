package com.example.mindstreak.feature.habit_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mindstreak.feature.home.AppViewModel
import com.example.mindstreak.feature.habit_detail.components.*
import androidx.core.graphics.toColorInt
import kotlin.math.roundToInt

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
    val uiState by appViewModel.uiState.collectAsState()
    val habit = remember(uiState.habits, habitId) { uiState.habits.find { it.id == habitId } }

    val texts = object {
        val notFound = "Habit not found"
        val returnHome = "Return Home"
        val backDesc = "Back"
        val moreDesc = "More"
        val reminderTemplate = "⏰ %s at %s"
        val statsLabels = listOf("Current", "Best", "Rate")
        val thisWeekTitle = "This Week"
        val daysInitialLabels = listOf("M", "T", "W", "T", "F", "S", "S")
        val heatmapMonth = "April 2026"
        val lessLabel = "Less"
        val moreLabel = "More"
        val completionRateTitle = "Completion Rate"
        val completionRateSubtitle = "Last 30 days"
        val comparisonText = "+8% vs last month"
        val editText = "Edit"
        val deleteDesc = "Delete habit"
    }

    if (habit == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(texts.notFound, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onBack) { Text(texts.returnHome) }
            }
        }
        return
    }

    val habitColor = remember(habit.color) {
        try {
            Color(habit.color.toColorInt())
        } catch (_: Exception) {
            Color(0xFF8B5CF6)
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
        HabitDetailHeader(
            title = habit.name,
            onBack = onBack,
            onMoreClick = { },
            backContentDescription = texts.backDesc,
            moreContentDescription = texts.moreDesc
        )

        HabitHeroCard(
            name = habit.name,
            emoji = habit.emoji,
            category = habit.category,
            frequency = habit.frequency,
            reminderTime = habit.reminderTime,
            streak = habit.streak.toString(),
            bestStreak = "34",
            completionRate = "${(habit.completionRate * 100).roundToInt()}%",
            habitColor = habitColor,
            statsLabels = texts.statsLabels,
            reminderTemplate = texts.reminderTemplate
        )

        HabitWeekProgress(
            weekHistory = habit.weekHistory,
            habitColor = habitColor,
            title = texts.thisWeekTitle,
            daysLabels = texts.daysInitialLabels
        )

        HabitHeatmap(
            monthTitle = texts.heatmapMonth,
            levelColors = levelColors,
            gridData = MONTH_GRID,
            lessLabel = texts.lessLabel,
            moreLabel = texts.moreLabel,
            dayLabels = texts.daysInitialLabels
        )

        HabitCompletionRateCard(
            completionRate = habit.completionRate,
            habitColor = habitColor,
            title = texts.completionRateTitle,
            subtitle = texts.completionRateSubtitle,
            comparisonText = texts.comparisonText
        )

        HabitDetailActions(
            onEdit = { },
            onDelete = {
                appViewModel.deleteHabit(habit.id)
                onBack()
            },
            editText = texts.editText,
            deleteContentDescription = texts.deleteDesc
        )
    }
}
