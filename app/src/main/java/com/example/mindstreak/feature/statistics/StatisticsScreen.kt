package com.example.mindstreak.feature.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.feature.home.AppViewModel
import com.example.mindstreak.feature.statistics.components.*

@Composable
fun StatisticsScreen(appViewModel: AppViewModel) {
    val uiState by appViewModel.uiState.collectAsState()

    val texts = object {
        val title = "Statistics"
        val dateText = "Wednesday, April 8"
        val weekStats = "33/42"
        val weekLabel = "This Week"
        val bestStreak = "34"
        val bestStreakLabel = "Best Streak"
        val avgDay = "4.7"
        val avgDayLabel = "Avg/Day"
        val tabs = listOf("Week", "Month", "Year")
        val barChartTitle = "Habits Completed"
        val doneLabel = "done"
        val totalLabel = "total"
        val trendTitle = "Completion Trend"
        val trendChange = "+12%"
        val rateLabel = "rate"
        val breakdownTitle = "Habit Breakdown"
        val summaryTitle = "Overall Today"
        val summarySubtitleTemplate = "%d of %d habits done"
        val moreToGoTemplate = "%d more to go!"
    }

    var selectedTab by remember { mutableStateOf(texts.tabs[0]) }

    val barChartData = remember(selectedTab) {
        when (selectedTab) {
            "Week" -> listOf(
                "Mon" to 0.6f,
                "Tue" to 1.0f,
                "Wed" to 0.7f,
                "Thu" to 1.0f,
                "Fri" to 0.8f,
                "Sat" to 0.5f,
                "Sun" to 0.6f
            )

            "Month" -> listOf("Wk 1" to 0.5f, "Wk 2" to 0.7f, "Wk 3" to 0.6f, "Wk 4" to 0.8f)
            "Year" -> listOf(
                "Jan" to 0.6f,
                "Feb" to 0.8f,
                "Mar" to 0.9f,
                "Apr" to 0.7f,
                "May" to 0f,
                "Jun" to 0f,
                "Jul" to 0f,
                "Aug" to 0f,
                "Sep" to 0f,
                "Oct" to 0f,
                "Nov" to 0f,
                "Dec" to 0f
            )

            else -> emptyList()
        }
    }

    val trendData = listOf(60f, 65f, 58f, 72f, 75f, 80f, 78f, 85f, 87f)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item { StatisticsHeader(texts.title, texts.dateText) }
        item {
            StatsCardsRow(
                texts.weekStats,
                texts.weekLabel,
                texts.bestStreak,
                texts.bestStreakLabel,
                texts.avgDay,
                texts.avgDayLabel
            )
        }
        item { StatisticsTabPicker(texts.tabs, selectedTab, { selectedTab = it }) }
        item {
            HabitsBarChart(
                texts.barChartTitle,
                barChartData,
                texts.doneLabel,
                texts.totalLabel,
                selectedTab == "Year"
            )
        }
        item {
            CompletionTrendChart(
                texts.trendTitle,
                texts.trendChange,
                texts.rateLabel,
                trendData
            )
        }

        item {
            androidx.compose.material3.Text(
                texts.breakdownTitle,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                fontSize = 15.dp.value.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        itemsIndexed(uiState.habits) { index, habit ->
            HabitBreakdownRow(habit.emoji, habit.name, habit.completionRate, habit.color, index)
        }
        item {
            SummaryRingCard(
                uiState.completionPercent,
                uiState.completedToday,
                uiState.totalHabits,
                texts.summaryTitle,
                texts.summarySubtitleTemplate,
                texts.moreToGoTemplate
            )
        }
    }
}
