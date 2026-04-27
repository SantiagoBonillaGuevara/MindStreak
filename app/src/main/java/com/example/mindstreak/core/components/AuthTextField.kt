package com.example.mindstreak.core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                placeholder,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                fontSize = 15.sp,
            )
        },
        leadingIcon = {
            Icon(
                leadingIcon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(12.dp),
        colors = authTextFieldColors(),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
        ),
    )
}

@Composable
fun authTextFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
    focusedContainerColor   = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
    unfocusedBorderColor    = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
    focusedBorderColor      = MaterialTheme.colorScheme.primary,
    cursorColor             = MaterialTheme.colorScheme.primary,
)
