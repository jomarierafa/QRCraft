package com.jvrcoding.qrcraft.qr_scanner

sealed interface QRScannerAction {
    data class SubmitCameraPermissionInfo(
        val acceptedCameraPermission: Boolean,
        val showCameraRationale: Boolean
    ): QRScannerAction
}