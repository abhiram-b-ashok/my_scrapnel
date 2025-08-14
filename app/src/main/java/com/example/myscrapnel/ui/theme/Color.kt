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