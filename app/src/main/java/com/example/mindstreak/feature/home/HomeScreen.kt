package com.example.mindstreak.feature.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mindstreak.core.components.EmptyState
import com.example.mindstreak.core.components.HabitCard
import com.example.mindstreak.core.components.HabitCardData
import com.example.mindstreak.core.components.QuoteCard
import com.example.mindstreak.core.navigation.Screen
import com.example.mindstreak.data.mock.MockData
import com.example.mindstreak.feature.home.components.*
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    appViewModel: AppViewModel,
    navController: NavController,
) {
    val state by appViewModel.uiState.collectAsState()
    val quote = MockData.MOTIVATIONAL_QUOTES[1]
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val texts = object {
        val dateText = "Wednesday, April 8"
        val greetingText = "Good morning, Alex 👋"
        val notificationsDesc = "Notifications"
        val streakLabel = "CURRENT STREAK"
        val streakDaysSuffix = "days 🔥"
        val nextMilestone = "Next milestone: 30 days"
        val bestStreak = "🏆 Best: 34"
        val goalProgress = "↑ 62% to goal"
        val dayZero = "Day 0"
        val dayGoal = "Day 30"
        val weekDays = listOf("M", "T", "W", "T", "F", "S", "S")
        val progressTitle = "Today's Progress"
        val progressTemplate = "%d%% complete"
        val habitsTitle = "Today's Habits"
        val addBtnText = "Add"
        val emptyEmoji = "🌱"
        val emptyTitle = "No habits yet"
        val emptyDesc = "Start building your streak by adding your first habit."
        val emptyBtn = "Add First Habit"
        val addNewHabitBtn = "Add a new habit"
        val allHabitsDoneMsg = "All habits done today! 🎉"
    }

    val handleToggle = { id: String ->
        val habit = state.habits.find { it.id == id }
        appViewModel.toggleHabit(id)
        if (habit != null && !habit.completedToday) {
            val willBeAllDone =
                state.habits.filter { it.id != id && it.completedToday }.size == state.habits.size - 1
            if (willBeAllDone) {
                scope.launch { snackbarHostState.showSnackbar(texts.allHabitsDoneMsg) }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            HomeHeader(
                texts.dateText,
                texts.greetingText,
                { navController.navigate(Screen.Notifications.route) },
                { navController.navigate(Screen.Profile.route) },
                texts.notificationsDesc
            )

            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(400, 100)) + slideInVertically(tween(400, 100)) { it / 3 },
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp)
            ) {
                StreakCard(
                    state.currentStreak,
                    texts.streakLabel,
                    texts.streakDaysSuffix,
                    texts.nextMilestone,
                    texts.bestStreak,
                    texts.goalProgress,
                    texts.dayZero,
                    "Day ${state.currentStreak}",
                    texts.dayGoal,
                    texts.weekDays,
                    2
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

            QuoteCard(quote = quote)

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

@Composable
private fun HabitsHeader(title: String, btnText: String, onAdd: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        TextButton(
            onClick = onAdd,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(4.dp))
            Text(
                btnText,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun HabitsList(
    habits: List<com.example.mindstreak.data.model.Habit>,
    onToggle: (String) -> Unit,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        habits.forEachIndexed { _, habit ->
            HabitCard(
                habit = HabitCardData(
                    habit.id,
                    habit.name,
                    habit.emoji,
                    habit.color,
                    habit.streak,
                    habit.completionRate,
                    habit.completedToday
                ),
                onToggle = { onToggle(it) },
                onClick = { onClick(habit.id) }
            )
        }
    }
}

@Composable
private fun AddNewHabitButton(text: String, onClick: () -> Unit) {
    Spacer(Modifier.height(8.dp))
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, fontWeight = FontWeight.Medium)
    }
}
