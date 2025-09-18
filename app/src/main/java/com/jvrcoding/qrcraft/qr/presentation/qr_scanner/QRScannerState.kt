package com.jvrcoding.qrcraft.qr.presentation.qr_scanner

data class QRScannerState(
    val isQRProcessing: Boolean = false,
    val isTorchOn: Boolean = false,
    val showErrorDialog: Boolean = false,
)
