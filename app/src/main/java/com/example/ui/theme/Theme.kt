package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = FarmGreenPrimaryDark,
    onPrimary = FarmOnGreenContainer,
    primaryContainer = FarmGreenContainerDark,
    onPrimaryContainer = FarmGreenContainer,
    secondary = HarvestGold,
    onSecondary = TextPrimaryLight,
    tertiary = SkyWaterBlue,
    background = SurfaceDark,
    surface = SurfaceCardDark,
    surfaceVariant = SurfaceCardElevatedDark,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = OutlineBorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = FarmGreenPrimary,
    onPrimary = SurfaceCardLight,
    primaryContainer = FarmGreenContainer,
    onPrimaryContainer = FarmOnGreenContainer,
    secondary = HarvestAmber,
    onSecondary = SurfaceCardLight,
    tertiary = SkyWaterBlue,
    background = CanvasSandLight,
    surface = SurfaceCardLight,
    surfaceVariant = SurfaceCardElevated,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = OutlineBorderLight
)

@Composable
fun AgriTwinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    AgriTwinTheme(darkTheme = darkTheme, content = content)
}
