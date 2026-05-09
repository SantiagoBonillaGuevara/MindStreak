package com.example.mindstreak.feature.statistics

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.mindstreak.R

data class StatisticsTexts(
    val title: String, val dateText: String, val weekStats: String,
    val weekLabel: String, val bestStreak: String, val bestStreakLabel: String,
    val avgDay: String, val avgDayLabel: String, val tabs: List<String>,
    val barChartTitle: String, val doneLabel: String, val totalLabel: String,
    val trendTitle: String, val trendChange: String, val rateLabel: String,
    val breakdownTitle: String, val summaryTitle: String,
    val summarySubtitleTemplate: String, val moreToGoTemplate: String
)

@Composable
fun rememberStatisticsTexts() = StatisticsTexts(
    title = stringResource(R.string.stats_title),
    dateText = stringResource(R.string.home_date),
    weekStats = "33/42",
    weekLabel = stringResource(R.string.stats_week_label),
    bestStreak = "34",
    bestStreakLabel = stringResource(R.string.stats_best_streak_label),
    avgDay = "4.7",
    avgDayLabel = stringResource(R.string.stats_avg_day_label),
    tabs = listOf(stringResource(R.string.stats_tab_week), stringResource(R.string.stats_tab_month), stringResource(R.string.stats_tab_year)),
    barChartTitle = stringResource(R.string.stats_bar_chart_title),
    doneLabel = stringResource(R.string.stats_done_label),
    totalLabel = stringResource(R.string.stats_total_label),
    trendTitle = stringResource(R.string.stats_trend_title),
    trendChange = "+12%",
    rateLabel = stringResource(R.string.stats_rate_label),
    breakdownTitle = stringResource(R.string.stats_breakdown_title),
    summaryTitle = stringResource(R.string.stats_summary_title),
    summarySubtitleTemplate = stringResource(R.string.stats_summary_subtitle_template),
    moreToGoTemplate = stringResource(R.string.stats_more_to_go_template)
)
