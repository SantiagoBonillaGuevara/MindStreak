package com.example.mindstreak.feature.profile

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mindstreak.core.components.*
import com.example.mindstreak.core.theme.*
import com.example.mindstreak.feature.profile.components.*
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.mindstreak.core.navigation.Screen
import androidx.annotation.RequiresApi
import com.example.mindstreak.feature.home.AppViewModel
import com.example.mindstreak.feature.notifications.NotificationPermissionHelper
import kotlinx.coroutines.launch

@SuppressLint("LocalContextGetResourceValueCall")
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit,
    onNavigateToNotifications: () -> Unit,
    viewModel: ProfileViewModel = viewModel(),
    appViewModel: AppViewModel
) {
    val user by viewModel.user.collectAsState()
    val appUiState by appViewModel.uiState.collectAsState()
    val texts = rememberProfileTexts()
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val darkMode = appUiState.isDarkMode ?: isSystemInDarkTheme()
    val notifications = appUiState.notificationsEnabled

    val permissionLauncher = NotificationPermissionHelper.rememberLauncher {
        appViewModel.setNotificationsEnabled(true)
    }

    LaunchedEffect(state) {
        if (state is ProfileState.LoggedOut) onNavigate(Screen.Auth.route)
        else if (state is ProfileState.Error) Toast.makeText(
            context,
            (state as ProfileState.Error).message,
            Toast.LENGTH_SHORT
        ).show()
    }

    if (user == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) { CircularProgressIndicator() }
        return
    }

    val stats = listOf(
        texts.statStreak to "${user!!.totalStreak}🔥",
        texts.statBest to "${user!!.bestStreak}",
        texts.statHabits to "${user!!.totalHabitsCompleted}",
        user!!.levelTitle to "${user!!.levelId}"
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        ProfileHeader(texts.title)
        ProfileHeroCard(
            user!!.avatarEmoji,
            user!!.name,
            user!!.username,
            user!!.university,
            user!!.levelId,
            user!!.levelTitle, // NUEVO
            texts.levelLabel,
            user!!.xpInCurrentLevel,
            user!!.nextLevelXpNeta,
            user!!.joinDate,
            texts.memberSince,
            texts.verified,
            texts.edit,
            user!!.isInstitutional,
            onEditClick = { onNavigate(Screen.EditProfile.route) }
        )
        ProfileStatsGrid(stats)
        Row(
            Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickNavButton(
                "🎁",
                texts.rewards,
                Modifier.weight(1f),
                enabled = user?.isInstitutional == true
            ) {
                onNavigate("rewards")
            }

            QuickNavButton(
                "🔔",
                texts.reminders,
                Modifier.weight(1f)
            ) { onNavigateToNotifications() }
        }
        SettingsSection(texts.prefSection) {
            SettingsToggleItem(
                Icons.Default.DarkMode,
                texts.darkModeLabel,
                texts.darkModeSub,
                HabitPurple,
                darkMode,
                onValueChange = { appViewModel.setDarkMode(it) })
            SettingsToggleItem(
                Icons.Default.Notifications,
                texts.notifLabel,
                if (notifications) texts.notifEnabled else texts.notifDisabled,
                HabitOrange,
                notifications,
                onValueChange = { enabled ->
                    if (enabled) NotificationPermissionHelper.checkAndRequestPermissions(context, permissionLauncher) { appViewModel.setNotificationsEnabled(true) }
                    else appViewModel.setNotificationsEnabled(false)
                })
        }
        SettingsSection(texts.accountSection) {
            SettingsClickItem(Icons.Default.Shield, texts.privacy, HabitTeal) {
                onNavigate(Screen.Privacy.route)
            }
            SettingsClickItem(Icons.Default.Star, texts.pro, HabitYellow, badge = texts.proBadge)
            SettingsClickItem(Icons.Default.Group, texts.refer, HabitPink, badge = texts.referBadge)
        }

        if (state is ProfileState.Loading) {
            Box(Modifier.fillMaxWidth(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator(Modifier.padding(20.dp))
            }
        }

        LogoutButton(texts.logout, { viewModel.logout() })
        SnackbarHost(hostState = snackbarHostState)
    }
}
