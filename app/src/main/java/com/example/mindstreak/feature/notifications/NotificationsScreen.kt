package com.example.mindstreak.feature.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.theme.*
import com.example.mindstreak.feature.home.AppViewModel
import com.example.mindstreak.feature.notifications.components.*

@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    appViewModel: AppViewModel
) {
    val uiState by appViewModel.uiState.collectAsState()

    var isMasterOn by remember { mutableStateOf(true) }
    var streakAlert by remember { mutableStateOf(true) }
    var dailySummary by remember { mutableStateOf(true) }
    var achievements by remember { mutableStateOf(true) }
    var social by remember { mutableStateOf(false) }

    val habitReminders = remember { mutableStateMapOf<String, Boolean>() }

    LaunchedEffect(uiState.habits) {
        if (habitReminders.isEmpty()) {
            uiState.habits.forEach { habitReminders[it.id] = true }
        }
    }

    val texts = object {
        val title = "Notifications"
        val backDesc = "Back"
        val masterTitle = "All Notifications"
        val masterActive = "Reminders are active"
        val masterMuted = "All muted"
        val alertTypesSection = "ALERT TYPES"
        val habitRemindersTitle = "Habit Reminders"

        val typeStreak = "Streak Alerts"
        val typeStreakDesc = "When streak is at risk"
        val typeSummary = "Daily Summary"
        val typeSummaryDesc = "End of day report"
        val typeAchievements = "Achievements"
        val typeAchievementsDesc = "Unlock notifications"
        val typeSocial = "Social Activity"
        val typeSocialDesc = "Friends & leaderboard"
    }

    val alertTypes = listOf(
        NotificationTypeData(
            Icons.Default.Whatshot,
            texts.typeStreak,
            texts.typeStreakDesc,
            HabitOrange,
            streakAlert,
            { streakAlert = it }),
        NotificationTypeData(
            Icons.Default.Notifications,
            texts.typeSummary,
            texts.typeSummaryDesc,
            HabitPurple,
            dailySummary,
            { dailySummary = it }),
        NotificationTypeData(
            Icons.Default.EmojiEvents,
            texts.typeAchievements,
            texts.typeAchievementsDesc,
            HabitYellow,
            achievements,
            { achievements = it }),
        NotificationTypeData(
            Icons.Default.People,
            texts.typeSocial,
            texts.typeSocialDesc,
            HabitTeal,
            social,
            { social = it })
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item { NotificationsHeader(texts.title, onBack, texts.backDesc) }

        item {
            MasterToggleCard(
                isMasterOn,
                { isMasterOn = it },
                texts.masterTitle,
                texts.masterActive,
                texts.masterMuted
            )
        }

        item { Spacer(Modifier.height(16.dp)) }

        item { AlertTypesSection(isMasterOn, texts.alertTypesSection, alertTypes) }

        item { Spacer(Modifier.height(24.dp)) }

        item {
            Text(
                texts.habitRemindersTitle,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(uiState.habits) { habit ->
            HabitReminderItem(
                emoji = habit.emoji,
                name = habit.name,
                timeText = habit.reminderTime,
                frequencyText = habit.frequency,
                isEnabled = habitReminders[habit.id] ?: false,
                onToggle = { habitReminders[habit.id] = it },
                activeColorHex = habit.color,
                isMasterOn = isMasterOn
            )
        }
    }
}
