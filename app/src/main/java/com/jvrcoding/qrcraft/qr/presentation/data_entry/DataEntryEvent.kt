package com.jvrcoding.qrcraft.qr.presentation.data_entry

import com.jvrcoding.qrcraft.qr.domain.qr.QrDetail

sealed interface DataEntryEvent {
    data class QrCodeGenerated(
        val qrDetail: QrDetail
    ): DataEntryEvent
}