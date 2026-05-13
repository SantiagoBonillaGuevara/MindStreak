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
import com.example.mindstreak.data.model.Habit
import com.example.mindstreak.feature.home.AppViewModel
import com.example.mindstreak.feature.create_habit.components.*
import kotlinx.coroutines.launch

import java.util.UUID

@Composable
fun CreateHabitScreen(appViewModel: AppViewModel, onBack: () -> Unit, onCreated: () -> Unit) {
    val state by appViewModel.uiState.collectAsState()
    val categories = state.categories
    
    var name by remember { mutableStateOf("") }
    var nameTouched by remember { mutableStateOf(false) }
    var selectedEmoji by remember { mutableStateOf("🏃") }
    // Initialize selectedCategory with the first category ID if available
    var selectedCategory by remember { mutableStateOf("") }
    
    // Update selectedCategory when categories are loaded
    LaunchedEffect(categories) {
        if (selectedCategory.isEmpty() && categories.isNotEmpty()) {
            selectedCategory = categories.first().id
        }
    }
    
    var selectedFreq by remember { mutableStateOf("Daily") }
    var selectedTime by remember { mutableStateOf("07:00") }
    var step by remember { mutableStateOf(CreateStep.DETAILS) }
    val texts = rememberCreateHabitTexts()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    val category = categories.find { it.id == selectedCategory } ?: categories.firstOrNull()

    val handleSave = {
        if (name.isNotBlank() && category != null) {
            appViewModel.addHabit(
                Habit(
                    id = UUID.randomUUID().toString(),
                    name = name.trim(),
                    emoji = selectedEmoji,
                    category = category.id, // Guardamos el ID de la categoría
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
                        onNameChange = { name = it },
                        nameTouched,
                        selectedEmoji,
                        onEmojiSelect = { selectedEmoji = it },
                        selectedCategory,
                        onCategorySelect = { selectedCategory = it },
                        category,
                        categories,
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
