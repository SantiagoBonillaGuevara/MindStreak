package com.example.mindstreak.feature.streak

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mindstreak.feature.home.AppViewModel
import com.example.mindstreak.feature.streak.components.*
import com.example.mindstreak.core.theme.HabitOrange

@SuppressLint("DefaultLocale")
@Composable
fun StreakScreen(appViewModel: AppViewModel, navController: NavController) {
    val uiState by appViewModel.uiState.collectAsState()
    val texts = rememberStreakTexts()
    val orangeMain = HabitOrange

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item { StreakHeader(texts.title) }
        item {
            StreakHeroCard(
                uiState.currentStreak,
                orangeMain,
                texts.streakLabel,
                texts.sinceText,
                texts.bestEverLabel,
                uiState.bestStreak.toString(),
                texts.totalLogsLabel,
                uiState.totalLogs.toString(),
                texts.thisMonthLabel,
                "${uiState.monthlyRate}%"
            )
        }

        item {
            CalendarCard(
                texts.streakDayLabel,
                texts.dayLabels,
                uiState.activeDaysInMonth,
                orangeMain
            )
        }
        item {
            StreakCtaCard(
                texts.ctaTitle,
                String.format(texts.ctaSubTemplate, uiState.currentStreak),
                orangeMain
            ) { navController.navigate("home") }
        }
        item {
            Text(
                texts.habitStreaksTitle,
                modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
        itemsIndexed(uiState.allHabits) { _, habit ->
            HabitStreakRow(
                habit.emoji,
                habit.name,
                habit.streak,
                habit.color
            )
        }
    }
}
