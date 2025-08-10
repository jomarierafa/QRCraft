package com.jvrcoding.qrcraft.qr.presentation.scan_result

sealed interface ScanResultEvent {
    data class ShareData(val data: String): ScanResultEvent
    data class CopyText(val data: String): ScanResultEvent
}