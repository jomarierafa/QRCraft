package com.jvrcoding.qrcraft.qr.presentation.qr_scanner

import android.net.Uri
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

sealed interface QRScannerAction {
    data class OnProcessImage(
        val imageProxy: ImageProxy,
        val imageAnalysis: ImageAnalysis
    ): QRScannerAction
    data object ToggleTorch: QRScannerAction
    data object OnDismissErrorDialog: QRScannerAction
    data class OnImagePick(val uri: Uri) : QRScannerAction

}