package com.example.myscrapnel.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    background = DarkBackground,
    surface = DarkBackground,
    primary = DarkAccent1,
    secondary = DarkAccent2,
    tertiary = DarkHighlight,
    onBackground = DarkText,
    onSurface = DarkText,
    onPrimary = DarkText,
    onSecondary = DarkText,
    onTertiary = DarkText,
)

private val LightColorScheme = lightColorScheme(
    background = LightBackground,
    surface = LightBackground,
    primary = LightAccent1,
    secondary = LightAccent2,
    tertiary = LightHighlight,
    onBackground = LightText,
    onSurface = LightText,
    onPrimary = LightText,
    onSecondary = LightText,
    onTertiary = LightText,
)

@Composable
fun MyScrapnelTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    ) {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (darkTheme) DarkColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}