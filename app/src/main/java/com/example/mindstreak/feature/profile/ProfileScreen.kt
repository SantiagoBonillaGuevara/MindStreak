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

@Composable
fun ProfileScreen(onNavigate: (String) -> Unit) {
    val user = MockData.USER
    var darkMode by remember { mutableStateOf(true) }
    var notifications by remember { mutableStateOf(true) }

    val texts = object {
        val title = "Profile"
        val levelLabel = "Level"
        val memberSince = "Member since"
        val verified = "✓ Verified Student"
        val edit = "Edit"
        val achievements = "Achievements"
        val social = "Social"
        val reminders = "Reminders"
        val prefSection = "Preferences"
        val darkModeLabel = "Dark Mode"
        val darkModeSub = "Always on"
        val notifLabel = "Notifications"
        val notifEnabled = "Enabled"
        val notifDisabled = "Disabled"
        val accountSection = "Account"
        val privacy = "Privacy & Data"
        val pro = "MindStreak Pro"
        val proBadge = "Upgrade"
        val refer = "Refer a Friend"
        val referBadge = "+50 XP"
        val logout = "Log Out"
        val statStreak = "Streak"
        val statBest = "Best"
        val statHabits = "Habits"
    }

    val stats = listOf(
        texts.statStreak to "${user.totalStreak}🔥",
        texts.statBest to "${user.bestStreak}",
        texts.statHabits to "${user.totalHabitsCompleted}",
        texts.levelLabel to "${user.level}"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        ProfileHeader(texts.title)

        ProfileHeroCard(
            avatarEmoji = user.avatarEmoji,
            name = user.name,
            username = user.username,
            university = user.university,
            level = user.level,
            levelLabel = texts.levelLabel,
            xp = user.xp,
            nextLevelXp = user.nextLevelXp,
            joinDate = user.joinDate,
            memberSinceLabel = texts.memberSince,
            verifiedLabel = texts.verified,
            editText = texts.edit
        )

        ProfileStatsGrid(stats)

        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
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

        SettingsSection(title = texts.prefSection) {
            SettingsToggleItem(
                icon = Icons.Default.DarkMode,
                label = texts.darkModeLabel,
                sub = texts.darkModeSub,
                color = HabitPurple,
                value = darkMode,
                onValueChange = { darkMode = it }
            )
            SettingsToggleItem(
                icon = Icons.Default.Notifications,
                label = texts.notifLabel,
                sub = if (notifications) texts.notifEnabled else texts.notifDisabled,
                color = HabitOrange,
                value = notifications,
                onValueChange = { notifications = it }
            )
        }

        SettingsSection(title = texts.accountSection) {
            SettingsClickItem(Icons.Default.Shield, texts.privacy, HabitTeal)
            SettingsClickItem(Icons.Default.Star, texts.pro, HabitYellow, badge = texts.proBadge)
            SettingsClickItem(Icons.Default.Group, texts.refer, HabitPink, badge = texts.referBadge)
        }

        LogoutButton(texts.logout, { onNavigate("auth") })
    }
}
