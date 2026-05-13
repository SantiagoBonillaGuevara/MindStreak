package com.example.mindstreak.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.mindstreak.R

data class HomeTexts(
    val dateText: String, val greetingText: String, val notificationsDesc: String,
    val streakLabel: String, val streakDaysSuffix: String, val nextMilestone: String,
    val goalProgress: String, val dayZero: String,
    val dayGoal: String, val dayText: String, val weekDays: List<String>,
    val progressTitle: String, val progressTemplate: String, val habitsTitle: String,
    val addBtnText: String, val emptyEmoji: String, val emptyTitle: String,
    val emptyDesc: String, val emptyBtn: String, val addNewHabitBtn: String,
    val allHabitsDoneMsg: String
)

@Composable
fun rememberHomeTexts() = HomeTexts(
    dateText = stringResource(R.string.home_date),
    greetingText = stringResource(R.string.home_greeting),
    notificationsDesc = stringResource(R.string.home_notifications),
    streakLabel = stringResource(R.string.home_streak_label),
    streakDaysSuffix = stringResource(R.string.home_streak_days_suffix),
    nextMilestone = stringResource(R.string.home_next_milestone),
    goalProgress = stringResource(R.string.home_goal_progress),
    dayZero = stringResource(R.string.home_day_zero),
    dayGoal = stringResource(R.string.home_day_goal),
    dayText = stringResource(R.string.home_day),
    weekDays = stringResource(R.string.home_week_days).split(","),
    progressTitle = stringResource(R.string.home_progress_title),
    progressTemplate = stringResource(R.string.home_progress_template),
    habitsTitle = stringResource(R.string.home_habits_title),
    addBtnText = stringResource(R.string.home_add_btn),
    emptyEmoji = "🌱",
    emptyTitle = stringResource(R.string.home_empty_title),
    emptyDesc = stringResource(R.string.home_empty_desc),
    emptyBtn = stringResource(R.string.home_empty_btn),
    addNewHabitBtn = stringResource(R.string.home_add_new_habit),
    allHabitsDoneMsg = stringResource(R.string.home_all_done_msg)
)
