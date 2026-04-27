package com.example.mindstreak.feature.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mindstreak.core.components.HabitCard
import com.example.mindstreak.core.components.HabitCardData
import com.example.mindstreak.core.components.ProgressRing
import com.example.mindstreak.core.navigation.Screen
import com.example.mindstreak.core.theme.*
import com.example.mindstreak.data.mock.MockData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    appViewModel: AppViewModel,
    navController: NavController,
) {
    val state by appViewModel.uiState.collectAsState()
    val quote = MockData.MOTIVATIONAL_QUOTES[1]

    // Snackbar — equivalente al toast de Sonner
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // lastTapped — equivalente al useState<string | null>(null)
    //var lastTapped by remember { mutableStateOf<String?>(null) }

    val handleToggle = { id: String ->
        val habit = state.habits.find { it.id == id }
        appViewModel.toggleHabit(id)
        //lastTapped = id
        scope.launch {
            delay(600)
            //lastTapped = null
        }
        // Toast solo cuando se completan TODOS los hábitos del día
        if (habit != null && !habit.completedToday) {
            val willBeAllDone = state.habits
                .filter { it.id != id && it.completedToday }.size == state.habits.size - 1
            if (willBeAllDone) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "All habits done today! 🎉",
                        duration = SnackbarDuration.Short,
                    )
                }
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
                .padding(bottom = 24.dp),
        ) {

            // ── Header ────────────────────────────────────────
            HomeHeader(
                onNotificationsClick = { navController.navigate(Screen.Notifications.route) },
                onProfileClick = { navController.navigate(Screen.Profile.route) },
            )

            // ── Streak Card ───────────────────────────────────
            var streakVisible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { streakVisible = true }

            AnimatedVisibility(
                visible = streakVisible,
                enter = fadeIn(tween(400, delayMillis = 100)) +
                        slideInVertically(tween(400, delayMillis = 100)) { it / 3 },
                modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 16.dp),
            ) {
                StreakCard(currentStreak = state.currentStreak)
            }

            // ── Today's Progress ──────────────────────────────
            var progressVisible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                delay(80)
                progressVisible = true
            }

            AnimatedVisibility(
                visible = progressVisible,
                enter = fadeIn(tween(400, delayMillis = 180)) +
                        slideInVertically(tween(400, delayMillis = 180)) { it / 3 },
                modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 16.dp),
            ) {
                TodayProgressCard(
                    completedToday = state.completedToday,
                    totalHabits = state.totalHabits,
                    completionPercent = state.completionPercent,
                )
            }

            // ── Motivational quote ────────────────────────────
            var quoteVisible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                delay(160)
                quoteVisible = true
            }

            AnimatedVisibility(
                visible = quoteVisible,
                enter = fadeIn(tween(500, delayMillis = 250)),
                modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 16.dp),
            ) {
                QuoteCard(quote = quote)
            }

            // ── Habits header ─────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Today's Habits",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                TextButton(
                    onClick = { navController.navigate(Screen.CreateHabit.route) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "Add",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    )
                }
            }

            // ── Habits list ───────────────────────────────────
            if (state.habits.isEmpty()) {
                EmptyHabitsState(
                    onAddClick = { navController.navigate(Screen.CreateHabit.route) }
                )
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    state.habits.forEachIndexed { i, habit ->
                        var itemVisible by remember { mutableStateOf(false) }
                        LaunchedEffect(habit.id) {
                            delay(100L + i * 60L)  // delay: 0.1 + i * 0.06
                            itemVisible = true
                        }
                        AnimatedVisibility(
                            visible = itemVisible,
                            enter = fadeIn(tween(300)) +
                                    slideInHorizontally(tween(300)) { -it / 4 },
                        ) {
                            HabitCard(
                                habit = HabitCardData(
                                    id = habit.id,
                                    name = habit.name,
                                    emoji = habit.emoji,
                                    color = habit.color,
                                    streak = habit.streak,
                                    completionRate = habit.completionRate,
                                    completedToday = habit.completedToday,
                                ),
                                onToggle = { handleToggle(it) },
                                onClick = {
                                    navController.navigate(
                                        Screen.HabitDetail.createRoute(habit.id)
                                    )
                                },
                            )
                        }
                    }
                }

                // ── Add habit CTA (botón dashed) ──────────────
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { navController.navigate(Screen.CreateHabit.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        // Nota: Compose no soporta border dashed nativo en OutlinedButton.
                        // Para el efecto dashed exacto necesitarías un Canvas custom.
                        // El borde sólido con alpha es suficientemente cercano visualmente.
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    ),
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Add a new habit", fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

// ─── Subcomponentes ──────────────────────────────────────────────────────────

@Composable
private fun HomeHeader(
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column {
            Text(
                text = "Wednesday, April 8",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )
            Text(
                text = "Good morning, Alex 👋",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 2.dp),
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp),
        ) {
            // Bell con dot — equivalente al span absolute naranja
            Box {
                FilledTonalIconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        modifier = Modifier.size(17.dp),
                    )
                }
                // Notification dot
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-6).dp, y = 6.dp)
                        .clip(CircleShape)
                        .background(HabitOrange)  // orange-500
                        .border(2.dp, MaterialTheme.colorScheme.background, CircleShape),
                )
            }

            // Avatar button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                HabitTeal,
                            )
                        )
                    )
                    .clickable(onClick = onProfileClick),
                contentAlignment = Alignment.Center,
            ) {
                Text("🧑‍💻", fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun StreakCard(currentStreak: Int) {
    val days = listOf("M", "T", "W", "T", "F", "S", "S")
    val todayIndex = 2 // Wednesday

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Box(modifier = Modifier.padding(16.dp)) {

            // Glow — equivalente al div absolute blur-2xl
            Box(
                modifier = Modifier
                    .size(144.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 20.dp, y = (-32).dp)
                    .clip(CircleShape)
                    .background(HabitOrange.copy(alpha = 0.15f))
                    .blur(24.dp),
            )

            Column {
                // Top row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = "CURRENT STREAK",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.8.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        )
                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            // Número animado con spring — equivalente a motion.span scale 0.5→1
                            var visible by remember { mutableStateOf(false) }
                            LaunchedEffect(Unit) {
                                delay(300)
                                visible = true
                            }
                            val scale by animateFloatAsState(
                                targetValue = if (visible) 1f else 0.5f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = 250f,
                                ),
                                label = "streakScale",
                            )
                            Text(
                                text = "$currentStreak",
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 48.sp),
                                color = HabitOrange,
                                modifier = Modifier.graphicsLayer {
                                    scaleX = scale; scaleY = scale
                                },
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "days 🔥",
                                style = MaterialTheme.typography.titleMedium,
                                color = HabitOrange,
                                modifier = Modifier.padding(bottom = 6.dp),
                            )
                        }
                        Text(
                            text = "Next milestone: 30 days",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(HabitOrange.copy(alpha = 0.1f))
                                .border(
                                    1.dp,
                                    HabitOrange.copy(alpha = 0.2f),
                                    CircleShape,
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                        ) {
                            Text(
                                text = "🏆 Best: 34",
                                style = MaterialTheme.typography.labelMedium,
                                color = HabitOrange,
                            )
                        }
                        Text(
                            text = "↑ 62% to goal",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Streak progress bar
                val progressWidth by animateFloatAsState(
                    targetValue = (currentStreak / 30f).coerceIn(0f, 1f),
                    animationSpec = tween(1000, delayMillis = 400, easing = FastOutSlowInEasing),
                    label = "streakBar",
                )
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progressWidth)
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(HabitOrange, HabitYellow)
                                    )
                                ),
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("Day 0", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                        Text("Day $currentStreak", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = HabitOrange)
                        Text("Day 30", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Week row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    days.forEachIndexed { i, d ->
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(
                                text = d,
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            i < todayIndex -> HabitOrange
                                            i == todayIndex -> Brush.horizontalGradient(
                                                listOf(HabitOrange, HabitYellow)
                                            ).let { HabitOrange } // simplificado
                                            else -> MaterialTheme.colorScheme.secondary
                                        }
                                    ),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TodayProgressCard(
    completedToday: Int,
    totalHabits: Int,
    completionPercent: Int,
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ProgressRing(
                progress = completionPercent.toFloat(),
                size = 72.dp,
                strokeWidth = 7.dp,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.secondary,
            ) {
                Text(
                    text = "$completedToday/$totalHabits",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Today's Progress",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "$completionPercent% complete",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.padding(top = 2.dp),
                )
                Spacer(Modifier.height(8.dp))

                // Progress bar animada
                val barWidth by animateFloatAsState(
                    targetValue = completionPercent / 100f,
                    animationSpec = tween(1200, delayMillis = 500, easing = FastOutSlowInEasing),
                    label = "progressBar",
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(barWidth)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary,
                                        HabitTeal,
                                    )
                                )
                            ),
                    )
                }
            }

            // Zap icon
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Default.Bolt,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun QuoteCard(quote: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
        ),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("💬", fontSize = 20.sp)
            Text(
                text = "\"$quote\"",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun EmptyHabitsState(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("🌱", fontSize = 48.sp)
        Text(
            text = "No habits yet",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Start building your streak by adding your first habit.",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 200.dp),
        )
        Button(
            onClick = onAddClick,
            modifier = Modifier.padding(top = 8.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(6.dp))
            Text("Add First Habit", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}