package com.example.mindstreak.feature.create_habit

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mindstreak.data.mock.MockData
import com.example.mindstreak.data.model.Habit
import com.example.mindstreak.feature.home.AppViewModel
import com.example.mindstreak.feature.create_habit.components.*
import kotlinx.coroutines.launch

@Composable
fun CreateHabitScreen(appViewModel: AppViewModel, onBack: () -> Unit, onCreated: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var nameTouched by remember { mutableStateOf(false) }
    var selectedEmoji by remember { mutableStateOf("🏃") }
    var selectedCategory by remember { mutableStateOf("fitness") }
    var selectedFreq by remember { mutableStateOf("Daily") }
    var selectedTime by remember { mutableStateOf("07:00") }
    var step by remember { mutableStateOf(CreateStep.DETAILS) }
    val texts = rememberCreateHabitTexts()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val category =
        MockData.CATEGORIES.find { it.id == selectedCategory } ?: MockData.CATEGORIES.first()

    val handleSave = {
        if (name.isNotBlank()) {
            appViewModel.addHabit(
                Habit(
                    id = System.currentTimeMillis().toString(),
                    name = name.trim(),
                    emoji = selectedEmoji,
                    category = selectedCategory,
                    color = category.color,
                    streak = 0,
                    completedToday = false,
                    frequency = selectedFreq,
                    completionRate = 0f,
                    reminderTime = selectedTime,
                    weekHistory = List(7) { false })
            )
            scope.launch { snackbarHostState.showSnackbar(texts.addedMsg.format(name.trim())) }; onCreated()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            CreateHabitFooter(
                step == CreateStep.DETAILS,
                name.isNotBlank(),
                { if (name.isNotBlank()) step = CreateStep.SCHEDULE },
                { step = CreateStep.DETAILS },
                { handleSave() },
                texts.nextBtn,
                texts.backBtn,
                texts.finishBtn
            )
        }) { padding ->
        Column(Modifier
            .fillMaxSize()
            .padding(padding)) {
            CreateHabitHeader(
                if (step == CreateStep.DETAILS) 0 else 1,
                CreateStep.entries.size,
                onBack,
                texts.screenTitle,
                texts.backDesc
            )
            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    (fadeIn(tween(300)) + slideInHorizontally(tween(300)) { it / 4 }).togetherWith(
                        fadeOut(tween(200))
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                label = "step"
            ) { curStep ->
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 16.dp)
                ) {
                    if (curStep == CreateStep.DETAILS) DetailsStep(
                        name,
                        { name = it },
                        nameTouched,
                        selectedEmoji,
                        { selectedEmoji = it },
                        selectedCategory,
                        { selectedCategory = it },
                        category,
                        EMOJIS,
                        texts.habitNameLabel,
                        texts.habitNamePlaceholder,
                        texts.habitNameError,
                        texts.iconLabel,
                        texts.categoryLabel,
                        texts.previewPlaceholder
                    )
                    else ScheduleStep(
                        selectedFreq,
                        { selectedFreq = it },
                        selectedTime,
                        { selectedTime = it },
                        name,
                        selectedEmoji,
                        category,
                        FREQUENCIES,
                        TIMES,
                        texts.freqLabel,
                        texts.timeLabel,
                        texts.summaryTitle,
                        texts.summaryPlaceholder,
                        texts.summaryReminderTemplate
                    )
                }
            }
        }
    }
}
