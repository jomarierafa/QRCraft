package com.jvrcoding.qrcraft.qr.presentation.qr_scanner

import com.google.mlkit.vision.barcode.common.Barcode

sealed interface QRScannerAction {
    data class SubmitCameraPermissionInfo(
        val acceptedCameraPermission: Boolean,
        val showCameraRationale: Boolean
    ): QRScannerAction
    data object DismissRationaleDialog: QRScannerAction
    data class OnSuccessfulScan(
        val barcode: Barcode,
    ): QRScannerAction
}