package com.example.mindstreak.feature.home

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mindstreak.core.components.*
import com.example.mindstreak.core.navigation.Screen
import com.example.mindstreak.data.mock.MockData
import com.example.mindstreak.feature.home.components.*
import kotlinx.coroutines.launch
import java.util.Locale

@SuppressLint("LocalContextResourcesRead", "LocalContextConfigurationRead")
@Composable
fun HomeScreen(appViewModel: AppViewModel, navController: NavController) {
    val state by appViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val texts = rememberHomeTexts()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        appViewModel.refreshData()
    }

    val handleToggle = { id: String ->
        appViewModel.toggleHabit(id)
        if (state.habits.find { it.id == id }?.completedToday == false && state.habits.filter { it.id != id && it.completedToday }.size == state.habits.size - 1) {
            scope.launch { snackbarHostState.showSnackbar(texts.allHabitsDoneMsg) }
        }
    }

    if (state.user == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            HomeHeader(
                dateText = texts.dateText,
                greetingText = "${texts.greetingText}, ${state.user!!.name.split(" ")[0]}!",
                profileEmoji = state.user!!.avatarEmoji,
                onNotificationsClick = { navController.navigate(Screen.Notifications.route) },
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                notificationsContentDescription = texts.notificationsDesc
            ) {
                val config = context.resources.configuration
                @Suppress("DEPRECATION") val locale =
                    Locale(if (config.locales[0].language == "es") "en" else "es")
                Locale.setDefault(locale); config.setLocale(locale)
                @Suppress("DEPRECATION")
                context.resources.updateConfiguration(config, context.resources.displayMetrics)
                (context as? android.app.Activity)?.recreate()
            }
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(400, 100)) + slideInVertically(tween(400, 100)) { it / 3 },
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp)
            ) {
                StreakCard(
                    currentStreak =  state.user!!.totalStreak,
                     texts.streakLabel,
                    texts.streakDaysSuffix,
                    nextMilestoneText =  texts.nextMilestone,
                    bestStreakText =  state.user!!.bestStreak.toString(),
                    goalText =  texts.goalProgress,
                    dayZeroLabel =  texts.dayZero,
                    dayCurrentLabel =  "${texts.dayText} ${state.user!!.totalStreak}",
                    dayGoalLabel =  texts.dayGoal,
                    texts.weekDays,
                    todayIndex =  (java.time.LocalDate.now().dayOfWeek.value + 6) % 7
                )
            }
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(400, 180)) + slideInVertically(tween(400, 180)) { it / 3 },
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp)
            ) {
                TodayProgressCard(
                    state.completedToday,
                    state.totalHabits,
                    state.completionPercent,
                    texts.progressTitle,
                    texts.progressTemplate
                )
            }
            QuoteCard(quote = MockData.MOTIVATIONAL_QUOTES[1])
            HabitsHeader(
                texts.habitsTitle,
                texts.addBtnText
            ) { navController.navigate(Screen.CreateHabit.route) }
            if (state.habits.isEmpty()) {
                EmptyState(
                    texts.emptyEmoji,
                    texts.emptyTitle,
                    texts.emptyDesc,
                    texts.emptyBtn
                ) { navController.navigate(Screen.CreateHabit.route) }
            } else {
                HabitsList(
                    state.habits,
                    handleToggle
                ) { navController.navigate(Screen.HabitDetail.createRoute(it)) }
                AddNewHabitButton(texts.addNewHabitBtn) { navController.navigate(Screen.CreateHabit.route) }
            }
        }
    }
}
