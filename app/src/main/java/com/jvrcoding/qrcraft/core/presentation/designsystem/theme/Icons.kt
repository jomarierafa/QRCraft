package com.jvrcoding.qrcraft.core.presentation.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.jvrcoding.qrcraft.R

val ShareIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_share)

val CopyIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_copy)

val ScanIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_scan)


val RefreshIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_clock_refresh)

val AddIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_plus_circle)