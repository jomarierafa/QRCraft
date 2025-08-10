package com.jvrcoding.qrcraft.qr.presentation.qr_scanner

import com.jvrcoding.qrcraft.core.presentation.util.UiText

sealed interface QRScannerEvent {
    data class Error(val error: UiText): QRScannerEvent
    data class ScanResult(val type: Int, val qrValue: String): QRScannerEvent
}