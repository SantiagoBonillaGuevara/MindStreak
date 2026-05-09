package com.example.mindstreak.feature.create_habit

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.mindstreak.R

data class CreateHabitTexts(
    val screenTitle: String, val backDesc: String, val nextBtn: String,
    val backBtn: String, val finishBtn: String, val habitNameLabel: String,
    val habitNamePlaceholder: String, val habitNameError: String,
    val iconLabel: String, val categoryLabel: String, val previewPlaceholder: String,
    val freqLabel: String, val timeLabel: String, val summaryTitle: String,
    val summaryPlaceholder: String, val summaryReminderTemplate: String,
    val addedMsg: String
)

@Composable
fun rememberCreateHabitTexts() = CreateHabitTexts(
    screenTitle = stringResource(R.string.create_habit_title),
    backDesc = stringResource(R.string.create_habit_back_desc),
    nextBtn = stringResource(R.string.create_habit_next_btn),
    backBtn = stringResource(R.string.create_habit_back_btn),
    finishBtn = stringResource(R.string.create_habit_finish_btn),
    habitNameLabel = stringResource(R.string.create_habit_name_label),
    habitNamePlaceholder = stringResource(R.string.create_habit_name_placeholder),
    habitNameError = stringResource(R.string.create_habit_name_error),
    iconLabel = stringResource(R.string.create_habit_icon_label),
    categoryLabel = stringResource(R.string.create_habit_category_label),
    previewPlaceholder = stringResource(R.string.create_habit_preview_placeholder),
    freqLabel = stringResource(R.string.create_habit_freq_label),
    timeLabel = stringResource(R.string.create_habit_time_label),
    summaryTitle = stringResource(R.string.create_habit_summary_title),
    summaryPlaceholder = stringResource(R.string.create_habit_summary_placeholder),
    summaryReminderTemplate = stringResource(R.string.create_habit_summary_reminder_template),
    addedMsg = stringResource(R.string.create_habit_added_msg)
)
