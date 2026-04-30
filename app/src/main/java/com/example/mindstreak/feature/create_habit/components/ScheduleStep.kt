package com.example.mindstreak.feature.create_habit.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mindstreak.data.model.Category

@Composable
fun ScheduleStep(
    selectedFreq: String,
    onFreqSelect: (String) -> Unit,
    selectedTime: String,
    onTimeSelect: (String) -> Unit,
    name: String,
    selectedEmoji: String,
    category: Category,
    frequencies: List<String>,
    times: List<String>,
    freqLabel: String,
    timeLabel: String,
    summaryTitle: String,
    summaryPlaceholder: String,
    summaryReminderTemplate: String,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        FrequencySelector(
            frequencies = frequencies,
            selectedFrequency = selectedFreq,
            onFrequencySelect = onFreqSelect,
            label = freqLabel
        )

        TimePicker(
            times = times,
            selectedTime = selectedTime,
            onTimeSelect = onTimeSelect,
            label = timeLabel
        )

        SummaryCard(
            name = name,
            emoji = selectedEmoji,
            category = category,
            frequency = selectedFreq,
            reminderTime = selectedTime,
            title = summaryTitle,
            placeholderName = summaryPlaceholder,
            reminderTemplate = summaryReminderTemplate
        )
    }
}
