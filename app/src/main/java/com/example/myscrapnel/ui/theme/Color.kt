package com.example.myscrapnel.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


val LightBackground = Color(0xFFFFF5E4)
val LightAccent1 = Color(0xFFFFC4D6)
val LightAccent2 = Color(0xFFCDB4DB)
val LightHighlight = Color(0xFFBDE0FE)
val LightText = Color(0xFF4F4F4F)


val DarkBackground = Color(0xFF1E1B1E)
val DarkAccent1 = Color(0xFFEFA1B5)
val DarkAccent2 = Color(0xFFB39BC8)
val DarkHighlight = Color(0xFF93C8E8)
val DarkText = Color(0xFFF0EAE4)



// 🌞 Light theme gradients
val LightPrimaryVertical = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFFFC4D6), // primary light
        Color(0xFFFFAFC6),
        Color(0xFFFF97B5)
    )
)

val LightPrimaryRadial = Brush.radialGradient(
    colors = listOf(
        Color(0xFFFF97B5),
        Color(0xFFFFAFC6),
        Color(0xFFFFC4D6)
    )
)

val LightSecondaryVertical = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFCDB4DB),
        Color(0xFFBFA0CF),
        Color(0xFFAC89C0)
    )
)

val LightSecondaryRadial = Brush.radialGradient(
    colors = listOf(
        Color(0xFFAC89C0),
        Color(0xFFBFA0CF),
        Color(0xFFCDB4DB)
    )
)

val LightTertiaryVertical = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFBDE0FE),
        Color(0xFFA9D3FB),
        Color(0xFF94C4F5)
    )
)

val LightTertiaryRadial = Brush.radialGradient(
    colors = listOf(
        Color(0xFF94C4F5),
        Color(0xFFA9D3FB),
        Color(0xFFBDE0FE)
    )
)


// 🌚 Dark theme gradients
val DarkPrimaryVertical = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFEFA1B5),
        Color(0xFFE888A2),
        Color(0xFFDE6C8F)
    )
)

val DarkPrimaryRadial = Brush.radialGradient(
    colors = listOf(
        Color(0xFFDE6C8F),
        Color(0xFFE888A2),
        Color(0xFFEFA1B5)
    )
)

val DarkSecondaryVertical = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFB39BC8),
        Color(0xFFA486BE),
        Color(0xFF9170AE)
    )
)

val DarkSecondaryRadial = Brush.radialGradient(
    colors = listOf(
        Color(0xFF9170AE),
        Color(0xFFA486BE),
        Color(0xFFB39BC8)
    )
)

val DarkTertiaryVertical = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF93C8E8),
        Color(0xFF7CB7DD),
        Color(0xFF64A3D2)
    )
)

val DarkTertiaryRadial = Brush.radialGradient(
    colors = listOf(
        Color(0xFF64A3D2),
        Color(0xFF7CB7DD),
        Color(0xFF93C8E8)
    )
)


val gradientColorsLight = listOf(Color(0xFFFFC4D6), Color(0xFFCDB4DB), Color(0xFFBDE0FE))
val gradientColorsDark = listOf(Color(0xFFEFA1B5), Color(0xFFB39BC8), Color(0xFF93C8E8))

val linearGradientBrushDark = Brush.linearGradient(colors = gradientColorsDark.map { it.copy(alpha = 0.5f) })
val linearGradientBrushLight = Brush.linearGradient(colors = gradientColorsLight.map { it.copy(alpha = 0.5f) })
val radialGradientBrushDark = Brush.radialGradient(colors = gradientColorsDark.map { it.copy(alpha = 0.5f) })
val radialGradientBrushLight = Brush.radialGradient(colors = gradientColorsLight.map { it.copy(alpha = 0.5f) })
val sweepGradientBrushDark = Brush.sweepGradient(colors = gradientColorsDark.map { it.copy(alpha = 0.5f) })
val sweepGradientBrushLight = Brush.sweepGradient(colors = gradientColorsLight.map { it.copy(alpha = 0.5f) })
val verticalGradientBrushDark = Brush.verticalGradient(colors = gradientColorsDark.map { it.copy(alpha = 0.5f) })
val verticalGradientBrushLight = Brush.verticalGradient(colors = gradientColorsLight.map { it.copy(alpha = 0.5f) })