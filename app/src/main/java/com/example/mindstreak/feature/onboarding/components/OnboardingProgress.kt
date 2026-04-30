package com.example.mindstreak.feature.onboarding.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingProgress(
    step: Int,
    totalSteps: Int,
    accentColor: Color,
    onNext: () -> Unit,
    buttonText: String,
    isLastStep: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 32.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(totalSteps) { index ->
                val width by animateDpAsState(
                    targetValue = if (index == step) 24.dp else 8.dp,
                    animationSpec = tween(300), label = "dot_$index",
                )
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(width)
                        .clip(CircleShape)
                        .background(if (index == step) accentColor else MaterialTheme.colorScheme.secondary),
                )
            }
        }

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor,
                contentColor = if (step == 0) MaterialTheme.colorScheme.onPrimary else Color.White,
            ),
        ) {
            Text(text = buttonText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(6.dp))
            Icon(
                imageVector = if (!isLastStep) Icons.Default.ChevronRight else Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null, modifier = Modifier.size(18.dp),
            )
        }
    }
}
