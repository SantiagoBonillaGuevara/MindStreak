package com.example.mindstreak.feature.habit_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HabitDetailActions(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    editText: String,
    deleteContentDescription: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onEdit,
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.secondary.copy(
                    alpha = 0.1f
                )
            )
        ) {
            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(8.dp))
            Text(editText, fontWeight = FontWeight.Bold)
        }
        OutlinedButton(
            onClick = onDelete,
            modifier = Modifier.size(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                contentColor = MaterialTheme.colorScheme.error
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(Icons.Default.Delete, contentDescription = deleteContentDescription)
        }
    }
}
