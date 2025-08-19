package com.jvrcoding.qrcraft.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min

enum class DeviceLayoutType {
    PORTRAIT, LANDSCAPE, TABLET
}

@Composable
fun rememberDeviceLayoutType(): DeviceLayoutType {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current

    val widthDp = with(density) { windowInfo.containerSize.width.toDp() }
    val heightDp = with(density) { windowInfo.containerSize.height.toDp() }

    return when {
        min(widthDp, heightDp) >= 600.dp -> DeviceLayoutType.TABLET
        widthDp > heightDp -> DeviceLayoutType.LANDSCAPE
        else -> DeviceLayoutType.PORTRAIT
    }
}