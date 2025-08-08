package com.jvrcoding.qrcraft.qr.presentation.qr_scanner

data class QRScannerState(
    val hasCameraPermission: Boolean = false,
    val showCameraRationale: Boolean = false,
)
