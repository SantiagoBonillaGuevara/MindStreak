package com.example.mindstreak.core.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ProgressRing(
    progress: Float,                // 0f–100f — mismo rango que el prop de React
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    strokeWidth: Dp = 8.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.secondary,
    content: @Composable (() -> Unit)? = null,
) {
    // Equivalente a motion animate strokeDashoffset
    // initial: circunferencia completa → animate: offset según progress
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 100f) / 100f,
        animationSpec = tween(
            durationMillis = 1200,    // duration: 1.2s
            delayMillis = 200,        // delay: 0.2s
            easing = FastOutSlowInEasing, // ease: 'easeOut'
        ),
        label = "progressRing",
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val strokePx = strokeWidth.toPx()
            val diameter = size.toPx() - strokePx
            //val radius = diameter / 2
            val topLeft = androidx.compose.ui.geometry.Offset(strokePx / 2, strokePx / 2)
            val arcSize = androidx.compose.ui.geometry.Size(diameter, diameter)

            // Track — círculo completo de fondo
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round),
            )
            // Progress — equivalente al motion.circle con strokeDashoffset
            // Canvas empieza en las 3h del reloj; -90f lo lleva a las 12h
            // igual que el transform: rotate(-90deg) del SVG en React
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round),
            )
        }
        // Equivalente al children en el centro del ring
        content?.invoke()
    }
}