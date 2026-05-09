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
fun NotificationsScreen(onBack: () -> Unit, appViewModel: AppViewModel) {
    val uiState by appViewModel.uiState.collectAsState()
    val texts = rememberNotificationsTexts()
    var isMasterOn by remember { mutableStateOf(true) }
    var streakAlert by remember { mutableStateOf(true) }
    var dailySummary by remember { mutableStateOf(true) }
    var achievements by remember { mutableStateOf(true) }
    var social by remember { mutableStateOf(false) }
    val habitReminders = remember { mutableStateMapOf<String, Boolean>() }
    LaunchedEffect(uiState.habits) {
        if (habitReminders.isEmpty()) uiState.habits.forEach {
            habitReminders[it.id] = true
        }
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
        Modifier
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
        item { AlertTypesSection(isMasterOn, texts.alertTypesSection, alertTypes) }
        item {
            Text(
                texts.habitRemindersTitle,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
        items(uiState.habits) { h ->
            HabitReminderItem(
                h.emoji,
                h.name,
                h.reminderTime,
                h.frequency,
                habitReminders[h.id] ?: false,
                { habitReminders[h.id] = it },
                h.color,
                isMasterOn
            )
        }
    }
}
