package com.example.mindstreak.feature.habit_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HabitDetailHeader(
    title: String,
    onBack: () -> Unit,
    onMoreClick: () -> Unit,
    backContentDescription: String,
    moreContentDescription: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FilledTonalIconButton(
            onClick = onBack,
            modifier = Modifier.size(36.dp),
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = backContentDescription,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
        )
        FilledTonalIconButton(
            onClick = onMoreClick,
            modifier = Modifier.size(36.dp),
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = moreContentDescription,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
