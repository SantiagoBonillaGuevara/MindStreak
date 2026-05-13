package com.example.mindstreak.feature.habit_detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.mindstreak.R

data class HabitDetailTexts(
    val notFound: String, val returnHome: String, val backDesc: String,
    val reminderTemplate: String, val statsLabels: List<String>,
    val thisWeekTitle: String, val daysInitialLabels: List<String>,
    val heatmapMonth: String, val lessLabel: String, val moreLabel: String,
    val completionRateTitle: String, val completionRateSubtitle: String,
    val comparisonText: String, val editText: String, val deleteDesc: String
)

@Composable
fun rememberHabitDetailTexts() = HabitDetailTexts(
    notFound = stringResource(R.string.habit_detail_not_found),
    returnHome = stringResource(R.string.habit_detail_return_home),
    backDesc = stringResource(R.string.habit_detail_back_desc),
    reminderTemplate = stringResource(R.string.habit_detail_reminder_template),
    statsLabels = listOf(stringResource(R.string.habit_detail_stat_current), stringResource(R.string.habit_detail_stat_best), stringResource(R.string.habit_detail_stat_rate)),
    thisWeekTitle = stringResource(R.string.habit_detail_this_week),
    daysInitialLabels = stringResource(R.string.home_week_days).split(","),
    heatmapMonth = stringResource(R.string.habit_detail_heatmap_month),
    lessLabel = stringResource(R.string.habit_detail_less),
    moreLabel = stringResource(R.string.habit_detail_more),
    completionRateTitle = stringResource(R.string.habit_detail_completion_rate_title),
    completionRateSubtitle = stringResource(R.string.habit_detail_completion_rate_subtitle),
    comparisonText = stringResource(R.string.habit_detail_comparison_text),
    editText = stringResource(R.string.habit_detail_edit),
    deleteDesc = stringResource(R.string.habit_detail_delete_desc)
)
