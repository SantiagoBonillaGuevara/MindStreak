package com.example.mindstreak.feature.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

@Composable
fun ProfileHeroCard(
    avatarEmoji: String,
    name: String,
    username: String,
    university: String,
    level: Int,
    levelLabel: String,
    xp: Int,
    nextLevelXp: Int,
    joinDate: String,
    memberSinceLabel: String,
    verifiedLabel: String,
    editText: String,
    modifier: Modifier = Modifier
) {
    val xpPercent = xp.toFloat() / nextLevelXp.toFloat()

    Box(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.surfaceVariant,
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(28.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Brush.linearGradient(listOf(HabitPurple, HabitTeal))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(avatarEmoji, fontSize = 32.sp)
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 4.dp, y = 4.dp)
                            .size(24.dp)
                            .background(HabitYellow, CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.background, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            level.toString(),
                            color = Color.Black,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Column(Modifier
                    .padding(start = 16.dp)
                    .weight(1f)) {
                    Text(
                        name,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
                    )
                    Text(
                        "@$username",
                        color = HabitPurple,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        university,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal)
                    )
                }

                Surface(
                    color = HabitPurple.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, HabitPurple.copy(alpha = 0.3f))
                ) {
                    Text(
                        editText,
                        color = HabitPurple,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(Modifier.padding(top = 20.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        "$levelLabel $level • Habit Architect",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                    Text(
                        "$xp / $nextLevelXp XP",
                        color = HabitYellow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { xpPercent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = HabitYellow,
                    trackColor = MaterialTheme.colorScheme.outlineVariant
                )
            }

            Row(Modifier.padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "$memberSinceLabel $joinDate",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
                Spacer(Modifier.width(8.dp))
                Surface(
                    color = HabitTeal.copy(alpha = 0.15f),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, HabitTeal.copy(alpha = 0.3f))
                ) {
                    Text(
                        verifiedLabel,
                        color = HabitTeal,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
