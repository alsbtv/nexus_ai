package com.nexus.ai.core.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val NexusDarkColorScheme = darkColorScheme(
    primary = TextPrimary,
    onPrimary = OledBlack,
    secondary = AccentBlue,
    onSecondary = TextPrimary,
    background = OledBlack,
    onBackground = TextPrimary,
    surface = DeepSurface,
    onSurface = TextPrimary,
    surfaceVariant = SecondarySurface,
    onSurfaceVariant = TextSecondary,
    outline = BorderColor
)

@Composable
fun NexusTheme(
    darkTheme: Boolean = true, // Enforced dark mode
    content: @Composable () -> Unit
) {
    val colorScheme = NexusDarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = OledBlack.toArgb()
            window.navigationBarColor = OledBlack.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NexusTypography,
        content = content
    )
}
