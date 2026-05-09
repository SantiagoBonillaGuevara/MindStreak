package com.example.mindstreak.feature.streak

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.mindstreak.R

data class StreakTexts(
    val title: String, val shieldLabel: String, val streakLabel: String,
    val sinceText: String, val bestEverLabel: String, val totalLogsLabel: String,
    val thisMonthLabel: String, val nextMilestoneTitle: String, val awayTemplate: String,
    val dayLabelTemplate: String, val milestonesTitle: String, val daysLabel: String,
    val remainingTemplate: String, val calendarMonth: String, val streakDayLabel: String,
    val dayLabels: List<String>, val ctaTitle: String, val ctaSubTemplate: String,
    val habitStreaksTitle: String
)

@Composable
fun rememberStreakTexts() = StreakTexts(
    title = stringResource(R.string.streak_title),
    shieldLabel = stringResource(R.string.streak_shield_label),
    streakLabel = stringResource(R.string.streak_day_streak),
    sinceText = stringResource(R.string.streak_since),
    bestEverLabel = stringResource(R.string.streak_best_ever),
    totalLogsLabel = stringResource(R.string.streak_total_logs),
    thisMonthLabel = stringResource(R.string.streak_this_month),
    nextMilestoneTitle = stringResource(R.string.streak_next_milestone),
    awayTemplate = stringResource(R.string.streak_away_template),
    dayLabelTemplate = stringResource(R.string.streak_day_label_template),
    milestonesTitle = stringResource(R.string.streak_milestones_title),
    daysLabel = stringResource(R.string.streak_days_label),
    remainingTemplate = stringResource(R.string.streak_remaining_template),
    calendarMonth = stringResource(R.string.streak_calendar_month),
    streakDayLabel = stringResource(R.string.streak_day_label),
    dayLabels = stringResource(R.string.home_week_days).split(","),
    ctaTitle = stringResource(R.string.streak_cta_title),
    ctaSubTemplate = stringResource(R.string.streak_cta_sub_template),
    habitStreaksTitle = stringResource(R.string.streak_habit_streaks_title)
)
