package com.project0.unboxwstyle.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = Accent,
    background = Background,
    surface = CardColor
)

@Composable
fun UnboxTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = DarkColors,
        content = content
    )
}