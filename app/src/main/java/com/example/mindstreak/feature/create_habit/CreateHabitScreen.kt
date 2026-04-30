package com.example.mindstreak.feature.create_habit

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import kotlinx.coroutines.launch
import com.example.mindstreak.feature.create_habit.components.*

@Composable
fun CreateHabitScreen(
    appViewModel: AppViewModel,
    onBack: () -> Unit,
    onCreated: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var nameTouched by remember { mutableStateOf(false) }
    var selectedEmoji by remember { mutableStateOf("🏃") }
    var selectedCategory by remember { mutableStateOf("fitness") }
    var selectedFreq by remember { mutableStateOf("Daily") }
    var selectedTime by remember { mutableStateOf("07:00") }
    var step by remember { mutableStateOf(CreateStep.DETAILS) }

    val category = MockData.CATEGORIES.find { it.id == selectedCategory }
        ?: MockData.CATEGORIES.first()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Centralized strings for future localization
    val texts = object {
        val screenTitle = "New Habit"
        val backDesc = "Back"
        val nextBtn = "Next: Schedule"
        val backBtn = "Back"
        val finishBtn = "Create Habit"
        val habitNameLabel = "Habit Name"
        val habitNamePlaceholder = "e.g. Morning Run"
        val habitNameError = "Please enter a habit name"
        val iconLabel = "Icon"
        val categoryLabel = "Category"
        val previewPlaceholder = "Your new habit"
        val freqLabel = "Frequency"
        val timeLabel = "Reminder Time"
        val summaryTitle = "SUMMARY"
        val summaryPlaceholder = "Your habit"
        val summaryReminderTemplate = "Reminder at %s every day"
    }

    val handleSave = {
        if (name.isNotBlank()) {
            appViewModel.addHabit(Habit(
                id = System.currentTimeMillis().toString(),
                name = name.trim(), emoji = selectedEmoji, category = selectedCategory,
                color = category.color, streak = 0, completedToday = false,
                frequency = selectedFreq, completionRate = 0f,
                reminderTime = selectedTime, weekHistory = List(7) { false }
            ))
            scope.launch { snackbarHostState.showSnackbar("\"${name.trim()}\" added!") }
            onCreated()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            CreateHabitFooter(
                isFirstStep = step == CreateStep.DETAILS,
                isValid = name.isNotBlank(),
                onNext = { if (name.isNotBlank()) step = CreateStep.SCHEDULE },
                onBack = { step = CreateStep.DETAILS },
                onFinish = { handleSave() },
                nextText = texts.nextBtn,
                backText = texts.backBtn,
                finishText = texts.finishBtn
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            CreateHabitHeader(
                currentStepIndex = if (step == CreateStep.DETAILS) 0 else 1,
                totalSteps = CreateStep.entries.size, 
                onBack = onBack,
                title = texts.screenTitle,
                backContentDescription = texts.backDesc
            )
            AnimatedContent(
                targetState = step,
                transitionSpec = { (fadeIn(tween(300)) + slideInHorizontally(tween(300)) { it / 4 }).togetherWith(fadeOut(tween(200))) },
                modifier = Modifier.weight(1f).fillMaxWidth(),
                label = "stepContent"
            ) { currentStep ->
                Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp).padding(bottom = 16.dp)) {
                    when (currentStep) {
                        CreateStep.DETAILS -> DetailsStep(
                            name, { name = it }, nameTouched, selectedEmoji, { selectedEmoji = it }, 
                            selectedCategory, { selectedCategory = it }, category, EMOJIS,
                            nameLabel = texts.habitNameLabel, namePlaceholder = texts.habitNamePlaceholder,
                            nameError = texts.habitNameError, emojiLabel = texts.iconLabel,
                            categoryLabel = texts.categoryLabel, previewPlaceholder = texts.previewPlaceholder
                        )
                        CreateStep.SCHEDULE -> ScheduleStep(
                            selectedFreq, { selectedFreq = it }, selectedTime, { selectedTime = it }, 
                            name, selectedEmoji, category, FREQUENCIES, TIMES,
                            freqLabel = texts.freqLabel, timeLabel = texts.timeLabel,
                            summaryTitle = texts.summaryTitle, summaryPlaceholder = texts.summaryPlaceholder,
                            summaryReminderTemplate = texts.summaryReminderTemplate
                        )
                    }
                }
            }
        }
    }
}
