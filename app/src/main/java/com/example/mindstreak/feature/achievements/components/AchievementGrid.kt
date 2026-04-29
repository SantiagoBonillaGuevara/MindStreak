package com.example.mindstreak.feature.achievements.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.data.model.Achievement
import com.example.mindstreak.feature.achievements.RarityUi
import kotlin.collections.forEach

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AchievementGrid(
    achievements: List<Achievement>,
    highlightedId: String?,
    onSelect: (String) -> Unit,
    onGetConfig: @Composable (Achievement) -> RarityUi
) {
    FlowRow(
        modifier = Modifier.padding(horizontal = 20.dp),
        maxItemsInEachRow = 3,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        achievements.forEach { ach ->
            val config = onGetConfig(ach)
            val isHighlighted = highlightedId == ach.id

            Column(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(0.85f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (ach.earned) config.bg else MaterialTheme.colorScheme.surface)
                    .border(
                        if (isHighlighted) 2.dp else 1.dp,
                        if (isHighlighted) config.color else if (ach.earned) config.color.copy(0.4f) else MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(20.dp)
                    )
                    .clickable { onSelect(ach.id) }
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(ach.emoji, fontSize = 30.sp)
                    if (!ach.earned) {
                        Box(
                            Modifier
                                .matchParentSize()
                                .background(
                                    MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Lock,
                                null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
                Text(
                    ach.name,
                    color = if (ach.earned) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                if (!ach.earned && ach.progress != null && ach.total != null) {
                    Spacer(Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { ach.progress.toFloat() / ach.total.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(3.dp)
                            .clip(CircleShape),
                        color = config.color,
                        trackColor = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}