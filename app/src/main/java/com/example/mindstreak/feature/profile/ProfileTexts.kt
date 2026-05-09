package com.example.mindstreak.feature.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.mindstreak.R

data class ProfileTexts(
    val title: String, val levelLabel: String, val memberSince: String,
    val verified: String, val edit: String, val achievements: String,
    val social: String, val reminders: String, val prefSection: String,
    val darkModeLabel: String, val darkModeSub: String, val notifLabel: String,
    val notifEnabled: String, val notifDisabled: String,
    val accountSection: String, val privacy: String, val pro: String,
    val proBadge: String, val refer: String, val referBadge: String,
    val logout: String, val statStreak: String, val statBest: String,
    val statHabits: String
)

@Composable
fun rememberProfileTexts() = ProfileTexts(
    title = stringResource(R.string.profile_title),
    levelLabel = stringResource(R.string.profile_level_label),
    memberSince = stringResource(R.string.profile_member_since),
    verified = stringResource(R.string.profile_verified),
    edit = stringResource(R.string.profile_edit),
    achievements = stringResource(R.string.profile_achievements),
    social = stringResource(R.string.profile_social),
    reminders = stringResource(R.string.profile_reminders),
    prefSection = stringResource(R.string.profile_pref_section),
    darkModeLabel = stringResource(R.string.profile_dark_mode),
    darkModeSub = stringResource(R.string.profile_dark_mode_sub),
    notifLabel = stringResource(R.string.profile_notif_label),
    notifEnabled = stringResource(R.string.profile_notif_enabled),
    notifDisabled = stringResource(R.string.profile_notif_disabled),
    accountSection = stringResource(R.string.profile_account_section),
    privacy = stringResource(R.string.profile_privacy),
    pro = stringResource(R.string.profile_pro),
    proBadge = stringResource(R.string.profile_pro_badge),
    refer = stringResource(R.string.profile_refer),
    referBadge = stringResource(R.string.profile_refer_badge),
    logout = stringResource(R.string.profile_logout),
    statStreak = stringResource(R.string.profile_stat_streak),
    statBest = stringResource(R.string.profile_stat_best),
    statHabits = stringResource(R.string.profile_stat_habits)
)
