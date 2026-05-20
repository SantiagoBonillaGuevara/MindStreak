package com.example.mindstreak.feature.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.feature.home.AppViewModel
import com.example.mindstreak.feature.statistics.components.*

@Composable
fun StatisticsScreen(appViewModel: AppViewModel) {
    val uiState by appViewModel.uiState.collectAsState()
    val texts = rememberStatisticsTexts()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    
    val processor = remember(uiState.habits) { StatisticsProcessor(uiState.habits) }
    val barChartData = remember(selectedTabIndex, uiState.habits) { 
        when (selectedTabIndex) {
            0 -> processor.getWeeklyBarData()
            1 -> processor.getMonthlyBarData()
            2 -> processor.getYearlyBarData()
            else -> emptyList()
        }
    }
    val trendData = remember(uiState.habits) { processor.getTrendData() }

    LazyColumn(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item { StatisticsHeader(texts.title, texts.dateText) }
        item {
            StatsCardsRow(
                weekStats = processor.getWeekStats(),
                weekLabel = texts.weekLabel,
                bestStreak = processor.getBestStreak(uiState.user?.bestStreak ?: 0),
                bestStreakLabel = texts.bestStreakLabel,
                avgDay = processor.getAvgPerDay(),
                avgDayLabel = texts.avgDayLabel
            )
        }
        item {
            StatisticsTabPicker(
                texts.tabs,
                texts.tabs[selectedTabIndex],
                { selectedTabIndex = texts.tabs.indexOf(it) })
        }
        item {
            HabitsBarChart(
                texts.barChartTitle,
                barChartData,
                texts.doneLabel,
                texts.totalLabel,
                selectedTabIndex == 2
            )
        }
        item {
            CompletionTrendChart(
                texts.trendTitle,
                processor.getTrendChange(),
                texts.rateLabel,
                trendData
            )
        }
        item {
            Text(
                texts.breakdownTitle,
                Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        itemsIndexed(uiState.habits) { idx, h ->
            HabitBreakdownRow(
                h.emoji,
                h.name,
                h.completionRate,
                h.color,
                idx
            )
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
