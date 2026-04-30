package com.example.mindstreak.feature.create_habit.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.components.SectionLabel
import kotlinx.coroutines.delay

@Composable
fun EmojiPicker(
    emojis: List<String>,
    selectedEmoji: String,
    onEmojiSelect: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SectionLabel(label)
        @OptIn(ExperimentalLayoutApi::class)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            emojis.forEach { emoji ->
                val isSelected = selectedEmoji == emoji
                var pressed by remember { mutableStateOf(false) }
                val scale by animateFloatAsState(
                    targetValue = if (pressed) 0.85f else 1f,
                    animationSpec = tween(80),
                    label = "emojiScale_$emoji",
                )
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .scale(scale)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            else
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                        )
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp),
                        )
                        .clickable {
                            pressed = true
                            onEmojiSelect(emoji)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(emoji, fontSize = 22.sp)
                }
                LaunchedEffect(pressed) {
                    if (pressed) {
                        delay(80)
                        pressed = false
                    }
                }
            }
        }
    }
}
