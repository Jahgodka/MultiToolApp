package com.example.tools.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BlueLightPrimary = Color(0xFF1E88E5)

val BlueDarkPrimary = Color(0xFF0E3E67)
val RedError = Color(0xFFD32F2F)

val DarkBackground = Color(0xFF1E1E1E)
val DarkSurface = Color(0xFF2C2C2C)
val DarkText = Color(0xFFFFFFFF)
val DarkTextSecondary = Color(0xFFAAAAAA)

val LightBackground = Color(0xFFF5F5F5)
val LightSurface = Color(0xFFFFFFFF)
val LightText = Color(0xFF121212)
val LightTextSecondary = Color(0xFF555555)
private val DarkColorScheme = darkColorScheme(
    primary = BlueDarkPrimary,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = DarkText,
    onBackground = DarkText,
    onSurface = DarkText,
    onSurfaceVariant = DarkTextSecondary,
    error = RedError
)

private val LightColorScheme = lightColorScheme(
    primary = BlueLightPrimary,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = Color.White,
    onBackground = LightText,
    onSurface = LightText,
    onSurfaceVariant = LightTextSecondary,
    error = RedError
)

@Composable
fun MultiToolAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}