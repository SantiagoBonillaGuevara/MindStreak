package com.example.mindstreak.feature.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mindstreak.core.components.*
import com.example.mindstreak.data.mock.MockData
import com.example.mindstreak.core.theme.*
import com.example.mindstreak.feature.profile.components.*

import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

import com.example.mindstreak.core.navigation.Screen

@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val user = MockData.USER
    val texts = rememberProfileTexts()
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    
    var darkMode by remember { mutableStateOf(true) }
    var notifications by remember { mutableStateOf(true) }

    LaunchedEffect(state) {
        if (state is ProfileState.LoggedOut) {
            onNavigate(Screen.Auth.route)
        } else if (state is ProfileState.Error) {
            Toast.makeText(context, (state as ProfileState.Error).message, Toast.LENGTH_SHORT).show()
        }
    }

    val stats = listOf(
        texts.statStreak to "${user.totalStreak}🔥",
        texts.statBest to "${user.bestStreak}",
        texts.statHabits to "${user.totalHabitsCompleted}",
        texts.levelLabel to "${user.level}"
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        ProfileHeader(texts.title)
        ProfileHeroCard(
            user.avatarEmoji,
            user.name,
            user.username,
            user.university,
            user.level,
            texts.levelLabel,
            user.xp,
            user.nextLevelXp,
            user.joinDate,
            texts.memberSince,
            texts.verified,
            texts.edit
        )
        ProfileStatsGrid(stats)
        Row(
            Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickNavButton(
                "🏆",
                texts.achievements,
                Modifier.weight(1f)
            ) { onNavigate("achievements") }
            QuickNavButton("👥", texts.social, Modifier.weight(1f)) { onNavigate("social") }
            QuickNavButton(
                "🔔",
                texts.reminders,
                Modifier.weight(1f)
            ) { onNavigate("notifications") }
        }
        SettingsSection(texts.prefSection) {
            SettingsToggleItem(
                Icons.Default.DarkMode,
                texts.darkModeLabel,
                texts.darkModeSub,
                HabitPurple,
                darkMode,
                { darkMode = it })
            SettingsToggleItem(
                Icons.Default.Notifications,
                texts.notifLabel,
                if (notifications) texts.notifEnabled else texts.notifDisabled,
                HabitOrange,
                notifications,
                { notifications = it })
        }
        SettingsSection(texts.accountSection) {
            SettingsClickItem(Icons.Default.Shield, texts.privacy, HabitTeal)
            SettingsClickItem(Icons.Default.Star, texts.pro, HabitYellow, badge = texts.proBadge)
            SettingsClickItem(Icons.Default.Group, texts.refer, HabitPink, badge = texts.referBadge)
        }
        
        if (state is ProfileState.Loading) {
            Box(Modifier.fillMaxWidth(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator(Modifier.padding(20.dp))
            }
        }
        
        LogoutButton(texts.logout, { viewModel.logout() })
    }
}
