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

@Composable
fun StreakCtaCard(
    title: String,
    subtitle: String,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(0.1f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(0.2f))
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Default.Bolt, null, tint = color, modifier = Modifier.size(24.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                Text(
                    subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                )
            }
            Icon(Icons.Default.ChevronRight, null, tint = color)
        }
    }
}
