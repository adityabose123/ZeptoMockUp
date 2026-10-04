package com.example.zeptomockup.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ZeptoPurple = Color(0xFF3B0A5E)
val ZeptoPurpleLight = Color(0xFF5B1F8C)
val ZeptoPink = Color(0xFFD6246E)
val ZeptoGreen = Color(0xFF1E8E3E)
val ZeptoBackground = Color(0xFFF5F3F8)

@Composable
fun ZeptoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = ZeptoPurple,
            secondary = ZeptoPink,
            background = ZeptoBackground,
            surface = Color.White,
        ),
        content = content,
    )
}
