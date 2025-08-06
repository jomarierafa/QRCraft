package com.jvrcoding.qrcraft.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    surface = Surface,
    surfaceContainerHigh = SurfaceHigher,
    onSurface = OnSurface
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