package com.example.mindstreak.feature.social

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mindstreak.core.theme.*
import com.example.mindstreak.data.mock.MockData
import com.example.mindstreak.feature.social.components.*

@Composable
private fun getRankColors() = listOf(HabitYellow, RankSilver, RankBronze)
private val RANK_LABELS = listOf("🥇", "🥈", "🥉")

@Composable
fun SocialScreen() {
    val texts = object {
        val title = "Social"
        val invite = "Invite"
        val searchPlaceholder = "Search friends..."
        val tabs = listOf("Leaderboard", "Activity", "Groups")
        val challengeTitle = "Weekly Challenge"
        val challengeSub = "Most habits logged wins!"
        val challengeTime = "3 days left"
        val youLabel = "YOU"
        val levelTemplate = "Lvl %d · %d habits/wk"
        val groupSubTemplate = "%d members · 🔥 %d day group streak"
        val createGroup = "+ Create a Group"
    }

    var selectedTab by remember { mutableStateOf(texts.tabs[0]) }
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SocialHeader(texts.title, texts.invite, { })
        SocialSearchBar(searchQuery, { searchQuery = it }, texts.searchPlaceholder)
        SocialTabPicker(texts.tabs, selectedTab, { selectedTab = it })

        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = { (fadeIn() + slideInVertically { it / 2 }).togetherWith(fadeOut()) },
            label = "socialContent"
        ) { currentTab ->
            when (currentTab) {
                texts.tabs[0] -> LeaderboardContent(
                    texts.challengeTitle,
                    texts.challengeSub,
                    texts.challengeTime,
                    texts.youLabel,
                    texts.levelTemplate
                )

                texts.tabs[1] -> ActivityContent()
                texts.tabs[2] -> GroupsContent(texts.groupSubTemplate, texts.createGroup)
            }
        }
    }
}

@Composable
fun LeaderboardContent(
    challengeTitle: String,
    challengeSub: String,
    challengeTime: String,
    youLabel: String,
    levelTemplate: String
) {
    val friends = MockData.FRIENDS
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
    ) {
        item {
            WeeklyChallengeBanner(
                challengeTitle,
                challengeSub,
                challengeTime
            ); Spacer(Modifier.height(20.dp))
        }
        item { PodiumRow(friends, RANK_LABELS, getRankColors()); Spacer(Modifier.height(24.dp)) }
        itemsIndexed(friends) { index, friend ->
            FriendRankItem(index, friend, RANK_LABELS, getRankColors(), youLabel, levelTemplate)
            Spacer(Modifier.height(10.dp))
        }
    }
}

data class ActivityData(
    val userName: String,
    val userEmoji: String,
    val action: String,
    val target: String,
    val time: String,
    val targetEmoji: String
)

@Composable
fun ActivityContent() {
    val activityItems = listOf(
        ActivityData("Emma Wilson", "👩‍🎨", "completed", "Morning Run", "8 min ago", "🏃"),
        ActivityData("Sarah Kim", "👩‍💻", "hit streak", "18 days!", "23 min ago", "🔥"),
        ActivityData("Mike Torres", "👨‍🎓", "unlocked", "Iron Will badge", "1h ago", "💪"),
        ActivityData("Emma Wilson", "👩‍🎨", "completed", "Meditate", "2h ago", "🧘")
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(activityItems) { item ->
            ActivityItemRow(
                item.userEmoji,
                item.userName,
                item.action,
                item.target,
                item.targetEmoji,
                item.time
            )
        }
    }
}

data class GroupData(
    val name: String,
    val members: Int,
    val streak: Int,
    val emoji: String,
    val color: Color
)

@Composable
fun GroupsContent(groupSubTemplate: String, createGroupText: String) {
    val groups = listOf(
        GroupData("MIT Wellness Club", 24, 14, "🎓", HabitPurple),
        GroupData("Morning Hustlers", 12, 7, "☀️", HabitOrange),
        GroupData("Sleep Scientists", 8, 21, "💤", HabitBlue)
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(groups) { group ->
            GroupItemRow(
                group.emoji,
                group.name,
                group.members,
                group.streak,
                group.color,
                groupSubTemplate
            )
        }
        item { CreateGroupButton(createGroupText, { }) }
    }
}
