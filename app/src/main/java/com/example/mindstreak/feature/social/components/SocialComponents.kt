package com.example.mindstreak.feature.social.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mindstreak.core.theme.*
import com.example.mindstreak.data.mock.MockData

data class ActivityData(val userName: String, val userEmoji: String, val action: String, val target: String, val time: String, val targetEmoji: String)
data class GroupData(val name: String, val members: Int, val streak: Int, val emoji: String, val color: Color)

@Composable
fun LeaderboardContent(challengeTitle: String, challengeSub: String, challengeTime: String, youLabel: String, levelTemplate: String) {
    val friends = MockData.FRIENDS
    val rankLabels = listOf("🥇", "🥈", "🥉")
    val rankColors = listOf(HabitYellow, RankSilver, RankBronze)
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)) {
        item { WeeklyChallengeBanner(challengeTitle, challengeSub, challengeTime); Spacer(Modifier.height(20.dp)) }
        item { PodiumRow(friends, rankLabels, rankColors); Spacer(Modifier.height(24.dp)) }
        itemsIndexed(friends) { index, friend -> FriendRankItem(index, friend, rankLabels, rankColors, youLabel, levelTemplate); Spacer(Modifier.height(10.dp)) }
    }
}

@Composable
fun ActivityContent() {
    val activityItems = listOf(
        ActivityData("Emma Wilson", "👩‍🎨", "completed", "Morning Run", "8 min ago", "🏃"),
        ActivityData("Sarah Kim", "👩‍💻", "hit streak", "18 days!", "23 min ago", "🔥"),
        ActivityData("Mike Torres", "👨‍🎓", "unlocked", "Iron Will badge", "1h ago", "💪"),
        ActivityData("Emma Wilson", "👩‍🎨", "completed", "Meditate", "2h ago", "🧘")
    )
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(activityItems) { item -> ActivityItemRow(item.userEmoji, item.userName, item.action, item.target, item.targetEmoji, item.time) }
    }
}

@Composable
fun GroupsContent(groupSubTemplate: String, createGroupText: String) {
    val groups = listOf(GroupData("MIT Wellness Club", 24, 14, "🎓", HabitPurple), GroupData("Morning Hustlers", 12, 7, "☀️", HabitOrange), GroupData("Sleep Scientists", 8, 21, "💤", HabitBlue))
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(groups) { group -> GroupItemRow(group.emoji, group.name, group.members, group.streak, group.color, groupSubTemplate) }
        item { CreateGroupButton(createGroupText, { }) }
    }
}
