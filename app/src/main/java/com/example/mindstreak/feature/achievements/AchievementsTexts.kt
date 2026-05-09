package com.example.mindstreak.feature.achievements

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.mindstreak.R

data class AchievementsTexts(
    val title: String, val subtitle: String, val earnedText: String,
    val lockedText: String, val xpText: String, val levelText: String,
    val lvlName: String, val recentlyEarned: String, val allText: String
)

@Composable
fun rememberAchievementsTexts() = AchievementsTexts(
    title = stringResource(R.string.achievements_title),
    subtitle = stringResource(R.string.achievements_subtitle),
    earnedText = stringResource(R.string.achievements_earned),
    lockedText = stringResource(R.string.achievements_locked),
    xpText = stringResource(R.string.achievements_xp_total),
    levelText = stringResource(R.string.achievements_level),
    lvlName = stringResource(R.string.achievements_lvl_name),
    recentlyEarned = stringResource(R.string.achievements_recently_earned),
    allText = stringResource(R.string.achievements_tab_all)
)
