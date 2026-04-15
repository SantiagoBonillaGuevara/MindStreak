package com.example.mindstreak.feature.create_habit

import android.graphics.Color.parseColor
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.data.mock.MockData
import com.example.mindstreak.data.model.Habit
import com.example.mindstreak.feature.home.AppViewModel
import kotlinx.coroutines.launch

private val EMOJIS = listOf(
    "🏃", "🧘", "💧", "📚", "😴", "📖", "🥗", "🏋️",
    "🚴", "🎯", "💊", "🧠", "☀️", "🌙", "🎵",
)
private val FREQUENCIES = listOf("Daily", "Weekdays", "3× a week", "Weekly")
private val TIMES = listOf(
    "06:00", "07:00", "08:00", "09:00",
    "12:00", "18:00", "20:00", "21:00", "22:00",
)

private enum class CreateStep { DETAILS, SCHEDULE }

@Composable
fun CreateHabitScreen(
    appViewModel: AppViewModel,
    onBack: () -> Unit,
    onCreated: () -> Unit,
) {
    // Todo estado local — sin ViewModel porque no necesita persistir
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
                    weekHistory = List(7) { false },
                )
            )
            scope.launch {
                snackbarHostState.showSnackbar(
                    "\"${name.trim()}\" added!",
                    duration = SnackbarDuration.Short,
                )
            }
            onCreated()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {

            // ── Header ────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                FilledTonalIconButton(
                    onClick = onBack,
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(16.dp),
                    )
                }

                Text(
                    text = "New Habit",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f),
                )

                // Step indicator — equivalente a los dots del header
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    CreateStep.entries.forEachIndexed { i, s ->
                        val isCurrent = s == step
                        val isPast = step == CreateStep.SCHEDULE && s == CreateStep.DETAILS
                        val width by animateDpAsState(
                            targetValue = if (isCurrent) 20.dp else 8.dp,
                            animationSpec = tween(300),
                            label = "stepDot_$i",
                        )
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(width)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isCurrent -> MaterialTheme.colorScheme.primary
                                        isPast    -> Color(0xFF14B8A6) // teal-400
                                        else      -> MaterialTheme.colorScheme.secondary
                                    }
                                ),
                        )
                    }
                }
            }

            // ── Contenido scrollable ──────────────────────────
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                AnimatedContent(
                    targetState = step,
                    transitionSpec = {
                        // initial: opacity 0, x 20 → animate: opacity 1, x 0
                        (fadeIn(tween(300)) + slideInHorizontally(tween(300)) { it / 4 })
                            .togetherWith(fadeOut(tween(200)))
                    },
                    label = "stepContent",
                ) { currentStep ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        when (currentStep) {
                            CreateStep.DETAILS  -> DetailsStep(
                                name = name,
                                onNameChange = { name = it },
                                nameTouched = nameTouched,
                                selectedEmoji = selectedEmoji,
                                onEmojiSelect = { selectedEmoji = it },
                                selectedCategory = selectedCategory,
                                onCategorySelect = { selectedCategory = it },
                                category = category,
                            )
                            CreateStep.SCHEDULE -> ScheduleStep(
                                selectedFreq = selectedFreq,
                                onFreqSelect = { selectedFreq = it },
                                selectedTime = selectedTime,
                                onTimeSelect = { selectedTime = it },
                                name = name,
                                selectedEmoji = selectedEmoji,
                                category = category,
                            )
                        }
                    }
                }
            }

            // ── Footer CTA ────────────────────────────────────
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp)
                    .navigationBarsPadding(),
            ) {
                when (step) {
                    CreateStep.DETAILS -> {
                        val hasName = name.isNotBlank()
                        Button(
                            onClick = {
                                nameTouched = true
                                if (hasName) step = CreateStep.SCHEDULE
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                            ),
                            contentPadding = PaddingValues(0.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = if (hasName) Brush.linearGradient(
                                            listOf(
                                                MaterialTheme.colorScheme.primary,
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                            )
                                        ) else Brush.linearGradient(
                                            listOf(
                                                MaterialTheme.colorScheme.secondary,
                                                MaterialTheme.colorScheme.secondary,
                                            )
                                        ),
                                        shape = RoundedCornerShape(16.dp),
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "Next: Schedule",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (hasName)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                )
                            }
                        }
                    }

                    CreateStep.SCHEDULE -> {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            FilledTonalButton(
                                onClick = { step = CreateStep.DETAILS },
                                modifier = Modifier.height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                            ) {
                                Text(
                                    "Back",
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                            Button(
                                onClick = { handleSave() },
                                enabled = name.isNotBlank(),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                ),
                                contentPadding = PaddingValues(0.dp),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.linearGradient(
                                                listOf(
                                                    MaterialTheme.colorScheme.primary,
                                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                                )
                                            ),
                                            shape = RoundedCornerShape(16.dp),
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    ) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp),
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                        )
                                        Text(
                                            "Create Habit",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─── Step 1: Details ────────────────────────────────────────────────────────

@Composable
private fun DetailsStep(
    name: String,
    onNameChange: (String) -> Unit,
    nameTouched: Boolean,
    selectedEmoji: String,
    onEmojiSelect: (String) -> Unit,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    category: com.example.mindstreak.data.model.Category,
) {
    // Habit name input
    Column {
        SectionLabel("Habit Name")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    RoundedCornerShape(16.dp),
                )
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(selectedEmoji, fontSize = 24.sp)
            TextField(
                value = name,
                onValueChange = onNameChange,
                placeholder = {
                    Text(
                        "e.g. Morning Run",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                        fontSize = 16.sp,
                    )
                },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor   = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor   = Color.Transparent,
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
        // Error — equivalente al p.text-destructive
        if (nameTouched && name.isBlank()) {
            Text(
                text = "Please enter a habit name",
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 6.dp, start = 4.dp),
            )
        }
    }

    // Emoji picker
    Column {
        SectionLabel("Icon")
        androidx.compose.foundation.layout.FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            EMOJIS.forEach { emoji ->
                val isSelected = selectedEmoji == emoji
                // whileTap scale: 0.85
                var pressed by remember { mutableStateOf(false) }
                val scale by animateFloatAsState(
                    targetValue = if (pressed) 0.85f else 1f,
                    animationSpec = tween(80),
                    label = "emojiScale_$emoji",
                )
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .scale(scale)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            else
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                        )
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp),
                        )
                        .clickable {
                            pressed = true
                            onEmojiSelect(emoji)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(emoji, fontSize = 22.sp)
                }
                LaunchedEffect(pressed) {
                    if (pressed) {
                        kotlinx.coroutines.delay(80)
                        pressed = false
                    }
                }
            }
        }
    }

    // Category grid
    Column {
        SectionLabel("Category")
        androidx.compose.foundation.layout.FlowRow(
            maxItemsInEachRow = 4,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            MockData.CATEGORIES.forEach { cat ->
                val isSelected = selectedCategory == cat.id
                val catColor = remember(cat.color) { Color(parseColor(cat.color)) }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected) catColor.copy(alpha = 0.12f)
                            else MaterialTheme.colorScheme.secondary
                        )
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) catColor else Color.Transparent,
                            shape = RoundedCornerShape(16.dp),
                        )
                        .clickable { onCategorySelect(cat.id) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(cat.emoji, fontSize = 20.sp)
                        Text(
                            text = cat.name,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) catColor
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }

    // Color preview
    val catColor = remember(category.color) { Color(parseColor(category.color)) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(catColor.copy(alpha = 0.06f))
            .border(1.dp, catColor.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(selectedEmoji, fontSize = 28.sp)
        Column {
            Text(
                text = name.ifBlank { "Your new habit" },
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = category.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = catColor,
            )
        }
    }
}

// ─── Step 2: Schedule ───────────────────────────────────────────────────────

@Composable
private fun ScheduleStep(
    selectedFreq: String,
    onFreqSelect: (String) -> Unit,
    selectedTime: String,
    onTimeSelect: (String) -> Unit,
    name: String,
    selectedEmoji: String,
    category: com.example.mindstreak.data.model.Category,
) {
    val catColor = remember(category.color) { Color(parseColor(category.color)) }

    // Frequency
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(bottom = 8.dp),
        ) {
            Icon(
                Icons.Default.Repeat,
                contentDescription = null,
                modifier = Modifier.size(13.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )
            SectionLabel("Frequency", withPadding = false)
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FREQUENCIES.forEach { freq ->
                val isSelected = selectedFreq == freq
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            else
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                        )
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(16.dp),
                        )
                        .clickable { onFreqSelect(freq) }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = freq,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface,
                    )
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                }
            }
        }
    }

    // Reminder time
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(bottom = 8.dp),
        ) {
            Icon(
                Icons.Default.Schedule,
                contentDescription = null,
                modifier = Modifier.size(13.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )
            SectionLabel("Reminder Time", withPadding = false)
        }
        androidx.compose.foundation.layout.FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TIMES.forEach { time ->
                val isSelected = selectedTime == time
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            else
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                        )
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp),
                        )
                        .clickable { onTimeSelect(time) }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = time,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    )
                }
            }
        }
    }

    // Summary card
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "SUMMARY",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.8.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.padding(bottom = 10.dp),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(catColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(selectedEmoji, fontSize = 22.sp)
                }
                Column {
                    Text(
                        text = name.ifBlank { "Your habit" },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = "${category.name} · $selectedFreq",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                        RoundedCornerShape(12.dp),
                    )
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = "Reminder at $selectedTime every day",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                )
            }
        }
    }
}

// ─── Helper ─────────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String, withPadding: Boolean = true) {
    Text(
        text = text.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.8.sp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        modifier = if (withPadding) Modifier.padding(bottom = 8.dp) else Modifier,
    )
}