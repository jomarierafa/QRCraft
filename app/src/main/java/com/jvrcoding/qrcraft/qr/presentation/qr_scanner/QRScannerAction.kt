package com.jvrcoding.qrcraft.qr.presentation.qr_scanner

sealed interface QRScannerAction {
    data class SubmitCameraPermissionInfo(
        val acceptedCameraPermission: Boolean,
        val showCameraRationale: Boolean
    ): QRScannerAction
    data object DismissRationaleDialog: QRScannerAction
}