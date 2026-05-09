package com.example.mindstreak.feature.notifications

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.mindstreak.R

data class NotificationsTexts(
    val title: String, val backDesc: String, val masterTitle: String,
    val masterActive: String, val masterMuted: String,
    val alertTypesSection: String, val habitRemindersTitle: String,
    val typeStreak: String, val typeStreakDesc: String,
    val typeSummary: String, val typeSummaryDesc: String,
    val typeAchievements: String, val typeAchievementsDesc: String,
    val typeSocial: String, val typeSocialDesc: String
)

@Composable
fun rememberNotificationsTexts() = NotificationsTexts(
    title = stringResource(R.string.notif_title),
    backDesc = stringResource(R.string.notif_back_desc),
    masterTitle = stringResource(R.string.notif_master_title),
    masterActive = stringResource(R.string.notif_master_active),
    masterMuted = stringResource(R.string.notif_master_muted),
    alertTypesSection = stringResource(R.string.notif_alert_types_section),
    habitRemindersTitle = stringResource(R.string.notif_habit_reminders_title),
    typeStreak = stringResource(R.string.notif_type_streak),
    typeStreakDesc = stringResource(R.string.notif_type_streak_desc),
    typeSummary = stringResource(R.string.notif_type_summary),
    typeSummaryDesc = stringResource(R.string.notif_type_summary_desc),
    typeAchievements = stringResource(R.string.notif_type_achievements),
    typeAchievementsDesc = stringResource(R.string.notif_type_achievements_desc),
    typeSocial = stringResource(R.string.notif_type_social),
    typeSocialDesc = stringResource(R.string.notif_type_social_desc)
)
