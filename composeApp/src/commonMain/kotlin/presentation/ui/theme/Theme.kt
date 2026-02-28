package com.quizapp.sailing.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme =
    lightColorScheme(
        primary = DeepSeaBlue,
        onPrimary = FoamWhite,
        primaryContainer = LightBlue,
        onPrimaryContainer = DarkBlue,
        secondary = OceanBlue,
        background = FoamWhite,
        surface = FoamWhite,
        onBackground = DarkBlue,
        onSurface = DarkBlue,
    )

private val DarkColorScheme =
    darkColorScheme(
        primary = SkyBlue,
        onPrimary = DarkBlue,
        primaryContainer = DeepSeaBlue,
        onPrimaryContainer = LightBlue,
        secondary = OceanBlue,
        background = DarkBlue,
        surface = DarkBlue,
        onBackground = FoamWhite,
        onSurface = FoamWhite,
    )

@Composable
fun SailingQuizTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
