package com.jvrcoding.qrcraft.qr.presentation.qr_scanner

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

sealed interface QRScannerAction {
    data class SubmitCameraPermissionInfo(
        val acceptedCameraPermission: Boolean,
        val showCameraRationale: Boolean
    ): QRScannerAction
    data object DismissRationaleDialog: QRScannerAction
    data class OnProcessImage(
        val imageProxy: ImageProxy,
        val imageAnalysis: ImageAnalysis
    ): QRScannerAction
}