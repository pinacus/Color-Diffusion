package com.colordiffusion.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(primary = Color(0xFFB64225), secondary = Color(0xFF5C5F70), background = Color(0xFFF8F7F4), surface = Color(0xFFF8F7F4))
private val DarkColors = darkColorScheme(primary = Color(0xFFFFB5A0), secondary = Color(0xFFC3C5D8), background = Color(0xFF17181B), surface = Color(0xFF17181B))

@Composable fun ColorDiffusionTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors, content = content)
}
