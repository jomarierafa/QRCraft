package com.jvrcoding.qrcraft.qr.presentation.qr_scanner

import com.jvrcoding.qrcraft.core.presentation.util.UiText
import com.jvrcoding.qrcraft.qr.domain.qr.QrDetailId

sealed interface QRScannerEvent {
    data class Error(val error: UiText): QRScannerEvent
    data class SuccessfulScan(
        val qrId: QrDetailId
    ): QRScannerEvent
}