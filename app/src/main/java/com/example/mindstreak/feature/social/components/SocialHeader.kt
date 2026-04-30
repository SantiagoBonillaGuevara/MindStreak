package com.example.mindstreak.feature.social.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mindstreak.core.theme.HabitPurple

@Composable
fun SocialHeader(
    title: String,
    inviteLabel: String,
    onInviteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )

        Surface(
            color = HabitPurple.copy(alpha = 0.15f),
            shape = RoundedCornerShape(12.dp),
            onClick = onInviteClick
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Default.PersonAdd,
                    null,
                    tint = HabitPurple,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    inviteLabel,
                    color = HabitPurple,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
