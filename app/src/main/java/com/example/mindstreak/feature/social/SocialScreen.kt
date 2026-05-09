package com.example.mindstreak.feature.social

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.mindstreak.feature.social.components.*

@Composable
fun SocialScreen() {
    val texts = rememberSocialTexts()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    Column(Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        SocialHeader(texts.title, texts.invite, { })
        SocialSearchBar(searchQuery, { searchQuery = it }, texts.searchPlaceholder)
        SocialTabPicker(
            texts.tabs,
            texts.tabs[selectedTabIndex],
            { selectedTabIndex = texts.tabs.indexOf(it) })
        AnimatedContent(
            targetState = selectedTabIndex,
            transitionSpec = { (fadeIn() + slideInVertically { it / 2 }).togetherWith(fadeOut()) },
            label = "socialContent"
        ) { idx ->
            when (idx) {
                0 -> LeaderboardContent(
                    texts.challengeTitle,
                    texts.challengeSub,
                    texts.challengeTime,
                    texts.youLabel,
                    texts.levelTemplate
                )

                1 -> ActivityContent()
                2 -> GroupsContent(texts.groupSubTemplate, texts.createGroup)
            }
        }
    }
}
