package com.example.mindstreak.core.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    background       = Background,
    onBackground     = Foreground,
    surface          = CardBackground,
    onSurface        = Foreground,
    primary          = Primary,
    onPrimary        = PrimaryFg,
    secondary        = Muted,
    onSecondary      = Foreground,
    error            = Destructive,
    onError          = PrimaryFg,
    surfaceVariant   = Accent,
    onSurfaceVariant = MutedFg,
)

private val DarkColors = darkColorScheme(
    background       = AppBackground,   // #060610 del body de index.css
    onBackground     = DarkForeground,
    surface          = DarkCard,
    onSurface        = DarkForeground,
    primary          = DarkForeground,
    onPrimary        = DarkBackground,
    secondary        = DarkMuted,
    onSecondary      = DarkForeground,
    error            = DarkDestructive,
    onError          = DarkForeground,
    surfaceVariant   = DarkMuted,
    onSurfaceVariant = DarkMutedFg,
)

@Composable
fun HabitsAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    // Colorea la status bar igual que el background
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content,
    )
}