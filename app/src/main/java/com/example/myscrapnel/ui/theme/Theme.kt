package com.example.myscrapnel.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColorScheme(
    background = Color(0xFFFFF5E4),
    primary = Color(0xFFFFC4D6),
    onPrimary = Color(0xFF4F4F4F),
    secondary = Color(0xFFCDB4DB),
    onSecondary = Color(0xFF3E3A47),
    tertiary = Color(0xFFBDE0FE),
    onTertiary = Color(0xFF2C2C2C),
    onBackground = Color(0xFF4F4F4F),
    surface = Color.White,
    onSurface = Color(0xFF333333)
)

val DarkColorScheme = darkColorScheme(
    background = Color(0xFF1E1B1E),
    primary = Color(0xFFEFA1B5),
    onPrimary = Color(0xFF1E1B1E),
    secondary = Color(0xFFB39BC8),
    onSecondary = Color(0xFF1E1B1E),
    tertiary = Color(0xFF93C8E8),
    onTertiary = Color(0xFF1E1B1E),
    onBackground = Color(0xFFF0EAE4),
    surface = Color(0xFF262326),
    onSurface = Color(0xFFEDEDED)
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
        typography = MyTypography,
        content = content
    )
}

