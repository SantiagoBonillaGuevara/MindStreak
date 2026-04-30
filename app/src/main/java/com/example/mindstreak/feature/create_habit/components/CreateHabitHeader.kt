package com.example.mindstreak.feature.create_habit.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.theme.HabitTeal

@Composable
fun CreateHabitHeader(
    currentStepIndex: Int,
    totalSteps: Int,
    onBack: () -> Unit,
    title: String,
    backContentDescription: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        FilledTonalIconButton(
            onClick = onBack,
            modifier = Modifier.size(36.dp),
            shape = CircleShape,
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = backContentDescription,
                modifier = Modifier.size(16.dp),
            )
        }

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(totalSteps) { i ->
                val isCurrent = i == currentStepIndex
                val isPast = i < currentStepIndex
                val width by animateDpAsState(
                    targetValue = if (isCurrent) 20.dp else 8.dp,
                    animationSpec = tween(300),
                    label = "stepDot_$i",
                )
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(width)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCurrent -> MaterialTheme.colorScheme.primary
                                isPast -> HabitTeal
                                else -> MaterialTheme.colorScheme.secondary
                            }
                        ),
                )
            }
        }
    }
}
