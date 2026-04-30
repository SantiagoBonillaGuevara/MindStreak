package com.example.mindstreak.feature.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LogoutButton(
    text: String,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onLogout,
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error.copy(
                alpha = 0.08f
            )
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
    ) {
        Icon(
            Icons.AutoMirrored.Filled.Logout,
            null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
    }
}
