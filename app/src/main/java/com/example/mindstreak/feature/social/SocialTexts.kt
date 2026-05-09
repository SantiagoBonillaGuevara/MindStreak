package com.example.mindstreak.feature.social

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.mindstreak.R

data class SocialTexts(
    val title: String, val invite: String, val searchPlaceholder: String,
    val tabs: List<String>, val challengeTitle: String, val challengeSub: String,
    val challengeTime: String, val youLabel: String, val levelTemplate: String,
    val groupSubTemplate: String, val createGroup: String
)

@Composable
fun rememberSocialTexts() = SocialTexts(
    title = stringResource(R.string.social_title),
    invite = stringResource(R.string.social_invite),
    searchPlaceholder = stringResource(R.string.social_search_placeholder),
    tabs = listOf(stringResource(R.string.social_tab_leaderboard), stringResource(R.string.social_tab_activity), stringResource(R.string.social_tab_groups)),
    challengeTitle = stringResource(R.string.social_challenge_title),
    challengeSub = stringResource(R.string.social_challenge_sub),
    challengeTime = stringResource(R.string.social_challenge_time),
    youLabel = stringResource(R.string.social_you_label),
    levelTemplate = stringResource(R.string.social_level_template),
    groupSubTemplate = stringResource(R.string.social_group_sub_template),
    createGroup = stringResource(R.string.social_create_group)
)
