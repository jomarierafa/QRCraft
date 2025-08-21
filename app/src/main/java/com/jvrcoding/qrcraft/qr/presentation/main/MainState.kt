package com.jvrcoding.qrcraft.qr.presentation.main

import com.jvrcoding.qrcraft.qr.presentation.main.model.BottomNavItem

data class MainState(
    val selectedScreenItem: BottomNavItem = BottomNavItem.SCAN_QR,
    val hasCameraPermission: Boolean = false,
    val showCameraRationale: Boolean = false
)
