package com.jvrcoding.qrcraft.core.presentation.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    surface = Surface,
    surfaceContainerHigh = SurfaceHigher,
    onSurface = OnSurface,
    outline = Outline
)

@Composable
fun QRCraftTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}