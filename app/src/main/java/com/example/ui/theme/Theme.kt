package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AsiCyan,
    onPrimary = AsiNavyDarkest,
    primaryContainer = AsiNavyCardElevated,
    onPrimaryContainer = AsiCyan,
    secondary = AsiCobalt,
    onSecondary = Color.White,
    secondaryContainer = AsiNavyCard,
    onSecondaryContainer = AsiTextPrimaryDark,
    tertiary = AsiEmerald,
    onTertiary = AsiNavyDarkest,
    background = AsiNavyDarkest,
    onBackground = AsiTextPrimaryDark,
    surface = AsiNavyDark,
    onSurface = AsiTextPrimaryDark,
    surfaceVariant = AsiNavyCard,
    onSurfaceVariant = AsiTextSecondaryDark,
    outline = AsiNavyBorder,
    outlineVariant = Color(0xFF1B2A4A),
    error = AsiRuby,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = AsiLightPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC7F3FA),
    onPrimaryContainer = Color(0xFF002025),
    secondary = AsiLightSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCE5FF),
    onSecondaryContainer = Color(0xFF051B40),
    tertiary = AsiEmeraldMuted,
    onTertiary = Color.White,
    background = AsiLightBg,
    onBackground = AsiLightTextPrimary,
    surface = AsiLightSurface,
    onSurface = AsiLightTextPrimary,
    surfaceVariant = AsiLightSurfaceElevated,
    onSurfaceVariant = AsiLightTextSecondary,
    outline = AsiLightBorder,
    outlineVariant = Color(0xFFCBD5E1),
    error = Color(0xFFBA1A1A),
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to deep navy research lab theme
    dynamicColor: Boolean = false, // Keep branded ASI palette
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
