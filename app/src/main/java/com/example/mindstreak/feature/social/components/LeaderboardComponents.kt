package com.example.mindstreak.feature.social.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.theme.*
import com.example.mindstreak.data.model.Friend

@Composable
fun WeeklyChallengeBanner(
    title: String,
    subtitle: String,
    timeLeft: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.Transparent,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, HabitPurple.copy(0.25f))
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(
                            HabitPurple.copy(0.15f),
                            HabitTeal.copy(0.15f)
                        )
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("⚔️", fontSize = 24.sp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
            }
            Text(timeLeft, color = HabitPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PodiumRow(
    friends: List<Friend>,
    rankLabels: List<String>,
    rankColors: List<Color>,
    modifier: Modifier = Modifier
) {
    val podiumOrder = listOf(friends[1], friends[0], friends[2])
    val heights = listOf(70.dp, 100.dp, 60.dp)
    val actualRanks = listOf(1, 0, 2)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        podiumOrder.forEachIndexed { index, friend ->
            val rankIndex = actualRanks[index]
            val isFirst = rankIndex == 0

            Column(
                modifier = Modifier.weight(if (isFirst) 1.2f else 1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(rankLabels[rankIndex], fontSize = if (isFirst) 20.sp else 16.sp)
                Text(friend.avatarEmoji, fontSize = if (isFirst) 36.sp else 28.sp)
                Text(
                    friend.name.split(" ")[0],
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = if (isFirst) 12.sp else 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "🔥${friend.streak}",
                    color = HabitOrange,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heights[index])
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    rankColors[rankIndex].copy(0.3f),
                                    rankColors[rankIndex].copy(0.1f)
                                )
                            )
                        )
                        .border(
                            1.dp,
                            rankColors[rankIndex].copy(0.4f),
                            RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        (rankIndex + 1).toString(),
                        color = rankColors[rankIndex],
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FriendRankItem(
    index: Int,
    friend: Friend,
    rankLabels: List<String>,
    rankColors: List<Color>,
    youLabel: String,
    levelTemplate: String, // e.g. "Lvl %d · %d habits/wk"
    modifier: Modifier = Modifier
) {
    val isTop3 = index < 3
    val isYou = friend.isYou

    Surface(
        modifier = modifier,
        color = if (isYou) HabitPurple.copy(0.1f) else MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            1.dp,
            if (isYou) HabitPurple.copy(0.4f) else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        if (isTop3) rankColors[index].copy(0.2f) else MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (isTop3) rankLabels[index] else (index + 1).toString(),
                    color = if (isTop3) rankColors[index] else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Text(
                friend.avatarEmoji,
                fontSize = 24.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        friend.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (isYou) {
                        Text(
                            youLabel,
                            modifier = Modifier
                                .padding(start = 6.dp)
                                .background(HabitPurple.copy(0.2f), CircleShape)
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            color = HabitPurple,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
                Text(
                    String.format(levelTemplate, friend.level, friend.weeklyHabits),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "🔥 ${friend.streak}",
                    color = HabitOrange,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${friend.points} pts",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp
                )
            }
        }
    }
}
