package com.jvrcoding.qrcraft.qr.presentation.data_entry

import com.jvrcoding.qrcraft.qr.domain.scanner.ScanResultDetail

sealed interface DataEntryEvent {
    data class QrCodeGenerated(
        val scanResultDetail: ScanResultDetail
    ): DataEntryEvent
}