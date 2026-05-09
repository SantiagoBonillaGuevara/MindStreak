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

@Composable
fun HabitDetailScreen(habitId: String, appViewModel: AppViewModel, onBack: () -> Unit) {
    val uiState by appViewModel.uiState.collectAsState()
    val habit = remember(uiState.habits, habitId) { uiState.habits.find { it.id == habitId } }
    val texts = rememberHabitDetailTexts()

    if (habit == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(texts.notFound, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onBack) { Text(texts.returnHome) }
            }
        }
        return
    }

    val hColor = remember(habit.color) {
        try {
            Color(habit.color.toColorInt())
        } catch (_: Exception) {
            Color(0xFF8B5CF6)
        }
    }
    val levelColors = listOf(
        MaterialTheme.colorScheme.secondary.copy(0.3f),
        hColor.copy(0.25f),
        hColor.copy(0.5f),
        hColor
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        HabitDetailHeader(habit.name, onBack, { }, texts.backDesc, texts.moreDesc)
        HabitHeroCard(
            habit.name,
            habit.emoji,
            habit.category,
            habit.frequency,
            habit.reminderTime,
            habit.streak.toString(),
            "34",
            "${(habit.completionRate * 100).roundToInt()}%",
            hColor,
            texts.statsLabels,
            texts.reminderTemplate
        )
        HabitWeekProgress(habit.weekHistory, hColor, texts.thisWeekTitle, texts.daysInitialLabels)
        HabitHeatmap(
            texts.heatmapMonth,
            levelColors,
            MONTH_GRID,
            texts.lessLabel,
            texts.moreLabel,
            texts.daysInitialLabels
        )
        HabitCompletionRateCard(
            habit.completionRate,
            hColor,
            texts.completionRateTitle,
            texts.completionRateSubtitle,
            texts.comparisonText
        )
        HabitDetailActions(
            { },
            { appViewModel.deleteHabit(habit.id); onBack() },
            texts.editText,
            texts.deleteDesc
        )
    }
}
